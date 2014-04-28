package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import com.utilsframework.android.R;

/**
 * User: Tikhonenko.S
 * Date: 21.11.13
 * Time: 17:31
 */
public class VideoPlayerView extends AbstractVideoPlayerView{
    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getVideoViewId() {
        return R.id.video_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_player;
    }
}
