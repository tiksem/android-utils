package com.utilsframework.android.media;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.util.Log;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.threading.ThrowingRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 15.08.14
 * Time: 17:57
 */
public class MediaSplicer {
    private static final String TAG = "MediaSplicer";
    private static final int MAX_SAMPLE_SIZE = 256 * 1024;

    public interface ProgressChangedListener {
        void onProgressChanged(long progress, long max);
    }

    private MediaMuxer mediaMuxer;
    private long currentProgress = 0;
    private long maxProgress = 0;
    private ProgressChangedListener progressChangedListener;
    private String outputPath;
    private volatile boolean cancelRequested = false;
    private volatile boolean muxerStarted = false;

    private enum  TrackType {
        AUDIO,
        VIDEO
    }

    public class Builder {
        private Queue<ThrowingRunnable<IOException>> splicingQueue = new ArrayDeque<ThrowingRunnable<IOException>>();
        private Map<String, MediaExtractor> mediaExtractorMap = new HashMap<String, MediaExtractor>();
        private long totalAudioDuration;
        private long totalVideoDuration;

        private EnumMap<TrackType, Integer> trackIndexMap = new EnumMap<TrackType, Integer>(TrackType.class);

        private int getTrackIndex(TrackType trackType, MediaFormat mediaFormat) {
            Integer trackIndex = trackIndexMap.get(trackType);
            if (trackIndex == null) {
                trackIndex = mediaMuxer.addTrack(mediaFormat);
                trackIndexMap.put(trackType, trackIndex);
            }

            return trackIndex;
        }

        private MediaExtractor getMediaExtractor(String trackPath) throws IOException {
            MediaExtractor mediaExtractor = mediaExtractorMap.get(trackPath);
            if (mediaExtractor == null) {
                mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(trackPath);
                mediaExtractorMap.put(trackPath, mediaExtractor);
            }

            return mediaExtractor;
        }

        private void addTrack(String path, final long trackStart, long maxTrackDurationUS, TrackType trackType)
                throws IOException {
            if(trackType == null){
                throw new NullPointerException();
            }

            final MediaExtractor mediaExtractor = getMediaExtractor(path);

            MediaFormat mediaFormat;
            if (trackType == TrackType.AUDIO) {
                mediaFormat = MediaUtils.selectAudioTrackOrThrow(path, mediaExtractor);
            } else {
                mediaFormat = MediaUtils.selectVideoTrackOrThrow(path, mediaExtractor);
            }

            long duration = mediaFormat.getLong(MediaFormat.KEY_DURATION);
            if(trackStart >= duration){
                throw new IllegalArgumentException("trackStart >= duration");
            }

            if(maxTrackDurationUS <= 0) {
                maxTrackDurationUS = Integer.MAX_VALUE;
            }

            final long presentationTimeOffset;
            maxTrackDurationUS = Math.min(maxTrackDurationUS, duration - trackStart);
            if(trackType == TrackType.AUDIO){
                presentationTimeOffset = totalAudioDuration;
                totalAudioDuration += maxTrackDurationUS;
            } else {
                presentationTimeOffset = totalVideoDuration;
                totalVideoDuration += maxTrackDurationUS;
            }

            final int trackIndex = getTrackIndex(trackType, mediaFormat);

            final long finalMaxTrackDurationUS = maxTrackDurationUS;
            splicingQueue.add(new ThrowingRunnable<IOException>() {
                @Override
                public void run() throws IOException {
                    mediaExtractor.seekTo(trackStart, MediaExtractor.SEEK_TO_NEXT_SYNC);
                    writeTrack(mediaExtractor, trackIndex, presentationTimeOffset, finalMaxTrackDurationUS);
                }
            });
        }

        public void addAudioTrack(String path, long trackStart, long maxTrackDurationUS) throws IOException {
            addTrack(path, trackStart, maxTrackDurationUS, TrackType.AUDIO);
        }

        public void addVideoTrack(String path, long trackStart, long maxTrackDurationUS) throws IOException {
            addTrack(path, trackStart, maxTrackDurationUS, TrackType.VIDEO);
        }
    }

    public Builder buildSplicing(String outputPath) throws IOException {
        mediaMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        this.outputPath = outputPath;
        return new Builder();
    }

    public void startSplicing(Builder builder, Tasks.OnException<IOException> onIoException) {
        if(builder.splicingQueue.isEmpty()){
            throw new IllegalStateException("Nothing has been set up for slicing");
        }

        mediaMuxer.start();
        muxerStarted = true;
        Tasks.executeThrowingRunnableQueue(builder.splicingQueue, onIoException);
        mediaMuxer.release();

        for(MediaExtractor mediaExtractor : builder.mediaExtractorMap.values()) {
            mediaExtractor.release();
        }
        muxerStarted = false;
    }

    public boolean splicingIsRunning() {
        return muxerStarted;
    }

    public void cancelSplicing() {
        if(!muxerStarted){
            throw new IllegalStateException("Call startSplicing before cancelSplicing");
        }

        cancelRequested = true;
    }

    public boolean isCancelRequested() {
        return cancelRequested;
    }

    private void writeTrack(MediaExtractor mediaExtractor, int trackIndex, long presentationTimeOffset,
                            long maxTrackDurationUS)
            throws IOException {

        boolean sawEOS = false;
        int bufferSize = MAX_SAMPLE_SIZE;
        int offset = 100;
        ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        while (!sawEOS) {
            if(cancelRequested){
                new File(outputPath).delete();
                break;
            }

            bufferInfo.offset = offset;
            bufferInfo.size = mediaExtractor.readSampleData(dstBuf, offset);
            if (bufferInfo.size < 0) {
                Log.d(TAG, "saw input EOS.");
                sawEOS = true;
                bufferInfo.size = 0;
                //increase total presentation time to total presentation time of last media file + 1 millisecond
            } else if (mediaExtractor.getSampleTime() > maxTrackDurationUS) {
                //create fake EOS, because video total presentation time is over
                Log.d(TAG, "fake input EOS.");
                sawEOS = true;
                bufferInfo.size = 0;
            } else {
                long sampleTime = mediaExtractor.getSampleTime();
                //presentation time calculate like total time of previous media file + getSampleTime()
                bufferInfo.presentationTimeUs = sampleTime + presentationTimeOffset;
                bufferInfo.flags = mediaExtractor.getSampleFlags();

                mediaMuxer.writeSampleData(trackIndex, dstBuf, bufferInfo);
                mediaExtractor.advance();

                currentProgress += sampleTime;

                if(progressChangedListener != null){
                    progressChangedListener.onProgressChanged(currentProgress, maxProgress);
                }
            }
        }
    }

    public ProgressChangedListener getProgressChangedListener() {
        return progressChangedListener;
    }

    public void setProgressChangedListener(ProgressChangedListener progressChangedListener) {
        this.progressChangedListener = progressChangedListener;
    }

    public enum SplicingState {
        BUILD,
        STARTED
    }

    public enum SplicingResult {
        SUCCESS,
        CANCELLED,
        FAILED
    }

    public interface AsyncBuilder {
        void addAudioTrack(String path, long trackStart, long maxTrackDurationUS);
        void addVideoTrack(String path, long trackStart, long maxTrackDurationUS);
    }

    public interface SplicingHandler {
        void onBuild(AsyncBuilder builder);
        void onProgressChanged(long current, long max);
        boolean onIOException(IOException e, SplicingState splicingState); // return true to stop splicing
        void onSplicingFinished(SplicingResult splicingResult);
    }

    public static MediaSplicer executeSplicingAsync(final String outPutPath, final SplicingHandler splicingHandler) {
        final MediaSplicer mediaSplicer = new MediaSplicer();
        final Handler handler = new Handler();
        final Object waiter = new Object();

        new Thread(new Runnable() {
            boolean exceptionHandlingResult;
            boolean oneSegmentSuccess = false;

            void handleException(final IOException e, final SplicingState splicingState) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (waiter) {
                            exceptionHandlingResult = splicingHandler.onIOException(e, splicingState);
                            waiter.notifyAll();
                        }
                    }
                });

                try {
                    synchronized (waiter) {
                        waiter.wait();
                    }
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            }

            @Override
            public void run() {
                mediaSplicer.setProgressChangedListener(new ProgressChangedListener() {
                    @Override
                    public void onProgressChanged(final long progress, final long max) {
                        oneSegmentSuccess = true;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                splicingHandler.onProgressChanged(progress, max);
                            }
                        });
                    }
                });

                Builder builder = null;
                try {
                    builder = mediaSplicer.buildSplicing(outPutPath);
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            splicingHandler.onIOException(e, SplicingState.BUILD);
                            splicingHandler.onSplicingFinished(SplicingResult.FAILED);
                        }
                    });
                    return;
                }

                final Builder finalBuilder = builder;
                splicingHandler.onBuild(new AsyncBuilder() {
                    @Override
                    public void addAudioTrack(String path, long trackStart, long maxTrackDurationUS) {
                        if(exceptionHandlingResult){
                            return;
                        }

                        try {
                            finalBuilder.addAudioTrack(path, trackStart, maxTrackDurationUS);
                        } catch (IOException e) {
                            handleException(e, SplicingState.BUILD);
                        }
                    }

                    @Override
                    public void addVideoTrack(String path, long trackStart, long maxTrackDurationUS) {
                        if(exceptionHandlingResult){
                            return;
                        }

                        try {
                            finalBuilder.addVideoTrack(path, trackStart, maxTrackDurationUS);
                        } catch (IOException e) {
                            handleException(e, SplicingState.BUILD);
                        }
                    }
                });

                if(exceptionHandlingResult){
                    return;
                }

                mediaSplicer.startSplicing(builder, new Tasks.OnException<IOException>() {
                    @Override
                    public boolean onException(IOException exception) {
                        handleException(exception, SplicingState.STARTED);
                        if(exceptionHandlingResult){
                            new File(outPutPath).delete();
                            splicingHandler.onSplicingFinished(SplicingResult.FAILED);
                        }

                        return exceptionHandlingResult;
                    }
                });

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        splicingHandler.onSplicingFinished(oneSegmentSuccess ?
                                SplicingResult.SUCCESS : SplicingResult.FAILED);
                    }
                });
            }
        }).start();

        return mediaSplicer;
    }
}
