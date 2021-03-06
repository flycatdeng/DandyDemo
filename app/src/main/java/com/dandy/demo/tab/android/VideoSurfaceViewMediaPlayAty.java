package com.dandy.demo.tab.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.helper.android.media.SurfaceVideoPlayerAider;

import java.io.File;

/**
 * <pre>
 *     使用SurfaceView加MediaPlayer播放视频的一个小demo
 *     如果时从sd卡读取的话一定记得1.视频资源路劲存在；2.AndroidManifest.xml中添加了读写sd卡的权限
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class VideoSurfaceViewMediaPlayAty extends BaseDemoAty {
    private static final String SDCARD_RES_PATH = "sdcard/test.mp4";
    private SurfaceView mSurfaceView;
    private SurfaceVideoPlayerAider mSurfaceVideoPlayerAider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(SDCARD_RES_PATH);
        if (!file.exists()) {
            TextView textView = new TextView(this);
            textView.setText("The path does not exist :" + SDCARD_RES_PATH);
            setContentView(textView);
            return;
        }
        mSurfaceView = new SurfaceView(this);
        mSurfaceVideoPlayerAider = new SurfaceVideoPlayerAider(this);
//        mSurfaceVideoPlayerAider.setResFromAssets("demo/video/test.mp4");
        mSurfaceVideoPlayerAider.setResFromSDCard(SDCARD_RES_PATH);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceVideoPlayerAider.onCreate(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        setContentView(mSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSurfaceVideoPlayerAider != null) {
            mSurfaceVideoPlayerAider.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSurfaceVideoPlayerAider != null) {
            mSurfaceVideoPlayerAider.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mSurfaceVideoPlayerAider != null) {
            mSurfaceVideoPlayerAider.onDestroy();
        }
        super.onDestroy();
    }
}
