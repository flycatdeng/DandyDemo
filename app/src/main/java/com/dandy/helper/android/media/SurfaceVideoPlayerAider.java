package com.dandy.helper.android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.PendingThreadAider;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *      本类主要是为Surface提供视频内容，使用本类需要new该对象，
 *      视频文件可以来自assets,sd卡，file对象，uri对象
 *      {@link #onCreate(Surface)}之前必须设置视频文件{@link #setResFromAssets(String)}等，否则会报错。
 *      可见，不可见，销毁时需要分别调用{@link #onResume()},{@link #onPause()},{@link #onDestroy()}否则会导致状态不对，而且内存泄露
 * </pre>
 * Created by flycatdeng on 2017/5/16.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class SurfaceVideoPlayerAider {
    private static final String TAG = "SurfaceVideoPlayerAider";
    private Context mContext;
    private MediaPlayer mMediaPlayer = null;
    private PendingThreadAider mDateSourceSets = new PendingThreadAider();

    public SurfaceVideoPlayerAider(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
    }

    public void setResFromAssets(final String resFromAsserts) {
        mDateSourceSets.addToPending(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setResFromAssets");
                try {
                    AssetFileDescriptor fileDescriptor = mContext.getApplicationContext().getAssets().openFd(resFromAsserts);
                    mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                    LogHelper.d(TAG, LogHelper.getThreadName() + " e=" + e.getMessage());
                }
            }
        });
    }

    public void setResFromUri(final Uri uri) {
        mDateSourceSets.addToPending(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setResFromUri");
                try {
                    mMediaPlayer.setDataSource(mContext, uri);
                } catch (IllegalArgumentException e) {
                    LogHelper.d(TAG, "setResFromUri failed 1 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (SecurityException e) {
                    LogHelper.d(TAG, "setResFromUri failed 2 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    LogHelper.d(TAG, "setResFromUri failed 3 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    LogHelper.d(TAG, "setResFromUri failed 4 e=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    public void setResFromSDCard(final String filePath) {
        mDateSourceSets.addToPending(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setResFromSDCard");
                try {
                    mMediaPlayer.setDataSource(filePath);
                } catch (IllegalArgumentException e) {
                    LogHelper.d(TAG, "setResFromSDCard failed 1 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (SecurityException e) {
                    LogHelper.d(TAG, "setResFromSDCard failed 2 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    LogHelper.d(TAG, "setResFromSDCard failed 3 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    LogHelper.d(TAG, "setResFromSDCard failed 4 e=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    public void setResFromFile(final File file) {
        mDateSourceSets.addToPending(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setResFromFile");
                if (file == null) {
                    throw new RuntimeException("file can not be null");
                }
                try {
                    mMediaPlayer.setDataSource(file.getAbsolutePath());
                } catch (IllegalArgumentException e) {
                    LogHelper.d(TAG, "setResFromFile failed 1 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (SecurityException e) {
                    LogHelper.d(TAG, "setResFromFile failed 2 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    LogHelper.d(TAG, "setResFromFile failed 3 e=" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    LogHelper.d(TAG, "setResFromFile failed 4 e=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    public void onCreate(Surface surface) {
        mMediaPlayer.setSurface(surface);
        surface.release();
        //mMediaPlayer.setDataSource()必须放在setSurface后执行，否则会报错
        mDateSourceSets.runPendings();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
//        mMediaPlayer.setVolume(0f, 0f);
        try {
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " e=" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onResume() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void onPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void onDestroy() {
        if(mDateSourceSets!=null){
            mDateSourceSets.clear();
            mDateSourceSets=null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }
}
