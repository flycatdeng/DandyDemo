package com.dandy.demo.tab.opengl;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.glengine.android.GLTextureView;
import com.dandy.helper.opengl.GLRenderer;
import com.dandy.helper.opengl.egl.AntiAliasingEGLConfigChooser;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/8
 */
public class GLTextureViewAty extends BaseDemoAty {
    private GLTextureView mGLTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLTextureView = new GLTextureView(this);
        mGLTextureView.setEGLConfigChooser(new AntiAliasingEGLConfigChooser());
        mGLTextureView.setPreserveEGLContextOnPause(true);
        mGLTextureView.setRenderMode(GLTextureView.RENDERMODE_WHEN_DIRTY);
        mGLTextureView.setRenderer(new GLRenderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        });
        setContentView(mGLTextureView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLTextureView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLTextureView.onPause();
    }
}
