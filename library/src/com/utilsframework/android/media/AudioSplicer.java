package com.utilsframework.android.media;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * User: Tikhonenko.S
 * Date: 15.08.14
 * Time: 17:57
 */
public class AudioSplicer {
    private MediaMuxer mediaMuxer;

    public AudioSplicer(MediaMuxer mediaMuxer) {
        this.mediaMuxer = mediaMuxer;
    }

    public void writeTrack(String trackPath) throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(trackPath);
        MediaUtils.selectAudioTrackOrThrow(trackPath, mediaExtractor);

//        boolean sawEOS = false;
//        int bufferSize = MAX_SAMPLE_SIZE;
//        int offset = 100;
//        ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
//        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//
//        Log.d(TAG, "Start slicing " + i + " audio file");
//
//        while (!sawEOS) {
//            bufferInfo.offset = offset;
//            bufferInfo.size = mediaExtractor.readSampleData(dstBuf, offset);
//            if (bufferInfo.size < 0) {
//                Log.d(TAG, "saw input EOS.");
//                sawEOS = true;
//                bufferInfo.size = 0;
//                //increase total presentation time to total presentation time of last media file + 1 millisecond
//                totalAudioPresentationTimeUs += lastPresentationTimeUs + 1;
//            } else if (totalAudioPresentationTimeUs + mediaExtractor.getSampleTime() > totalVideoPresentationTimeUs) {
//                //create fake EOS, because video total presentation time is over
//                Log.d(TAG, "fake input EOS.");
//                sawEOS = true;
//                isVPTOver = true;
//                bufferInfo.size = 0;
//            } else {
//                lastPresentationTimeUs = mediaExtractor.getSampleTime();
//                //presentation time calculate like total time of previous media file + getSampleTime()
//                bufferInfo.presentationTimeUs = totalAudioPresentationTimeUs + lastPresentationTimeUs;
//                bufferInfo.flags = mediaExtractor.getSampleFlags();
//
//                String mime = mediaExtractor.getTrackFormat(mediaExtractor.getSampleTrackIndex()).getString(MediaFormat.KEY_MIME);
//                int trackIndex = muxerIndexMap.get(mime);
//                //Log.d(TAG, "Frame (" + frameCount + ") PresentationTimeUs:" + bufferInfo.presentationTimeUs);
//                muxer.writeSampleData(trackIndex, dstBuf, bufferInfo);
//                mediaExtractor.advance();
//
//                onAudioProgressChanged(bufferInfo.presentationTimeUs, totalVideoPresentationTimeUs);
//
//                Log.d(TAG, "Audio frame (" + frameCount + ") " +
//                        "PresentationTimeUs:" + bufferInfo.presentationTimeUs +
//                        " Flags:" + bufferInfo.flags +
//                        " TrackIndex:" + trackIndex +
//                        " Size(KB) " + bufferInfo.size / 1024);
//
//                frameCount++;
//            }
//        }
//
//        Log.d(TAG, "Finish slicing " + i + " audio file");
//        mediaExtractor.release();
    }
}
