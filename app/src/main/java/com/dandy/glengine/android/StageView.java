package com.dandy.glengine.android;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;

import com.dandy.glengine.IGLActor;
import com.dandy.glengine.Stage;
import com.dandy.helper.opengl.egl.AntiAliasingEGLConfigChooser;
import com.dandy.helper.opengl.egl.GLSVConverter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class StageView extends GLSurfaceView {
    protected Stage mStage;
    private int mRenderMode = RENDERMODE_WHEN_DIRTY;
    private EGLConfigChooser mEGLConfigChooser;

    public StageView(Context context) {
        super(context);
        init(context);
    }

    public StageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mStage = new Stage(context);
        mStage.setRequestRenderListener(new IGLActor.RequestRenderListener() {
            @Override
            public void onRequestRenderCalled() {
                requestRender();
            }
        });
        this.setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setPreserveEGLContextOnPause(true);//如果没有这一句，那onPause之后再onResume屏幕将会是黑屏滴
        }
    }

    @Override
    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        super.setEGLConfigChooser(configChooser);
        mEGLConfigChooser = configChooser;
    }

    public void initRenderer() {
        if (mEGLConfigChooser == null) {
            mEGLConfigChooser = GLSVConverter.convertEGLConfigChooser(new AntiAliasingEGLConfigChooser());
        }
        setRenderer(mRenderer); // 设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    @Override
    public void setRenderMode(int renderMode) {
        super.setRenderMode(renderMode);
        mRenderMode = renderMode;
    }

    public Stage getStage() {
        return mStage;
    }


    private Renderer mRenderer = new Renderer() {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mStage.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mStage.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清除深度缓冲与颜色缓冲
            mStage.onDrawFrame();
        }
    };

    @Override
    public void onPause() {
//        if (mRenderMode == RENDERMODE_CONTINUOUSLY) {
//            super.onPause();
//        }
        super.onPause();
        mStage.onPause();
    }

    @Override
    public void onResume() {
//        if (mRenderMode == RENDERMODE_CONTINUOUSLY) {
//            super.onResume();
//        }
        super.onResume();
        mStage.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    public void onDestroy() {
        if (mRenderer != null) {
            mRenderer = null;
        }
        if (mStage != null) {
            mStage.onDestroy();
            super.onDetachedFromWindow();
            mStage = null;
        }
    }

    /**
     * <pre>
     *  设置透明背景的方法，根据实际情况，可能setEGLConfigChooser中的alpha可能要设置成0
     *  再者就是这个方法需要在setRenderer之前调用才有效
     * </pre>
     */
    public void setTranslucent() {
        // 设置背景透明，否则一般加载时间长的话会先黑一下，但是也有问题，就是在它之上无法再有View了，因为它是top的，用的时候需要注意，必要的时候将其设置为false
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }
}
