package com.dandy.demo.tab.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.glengine.SimpleImage;

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

public class SimpleImageAty extends BaseDemoAty {
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new StageView(this);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();//唤醒渲染线程
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();//暂停渲染线程
    }

    /**
     * 一个GLSurfaceView的子类
     */
    class StageView extends GLSurfaceView {
        private SimpleImage mImage;

        public StageView(Context context) {
            super(context);
            mImage = SimpleImage.createFromAssets(context, "demo/test_image.jpg");
            init(context);
        }

        private void init(Context context) {
            this.setEGLContextClientVersion(2); // 设置使用OPENGL ES3.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setPreserveEGLContextOnPause(true);//如果没有这一句，那onPause之后再onResume屏幕将会是黑屏滴
            }
            setRenderer(mRenderer);//设置渲染器
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//请求的时候才去渲染一次
        }

        /**
         * 渲染器
         */
        private Renderer mRenderer = new Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);//设置背景色，黄色
                mImage.onSurfaceCreated();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
//                GLES20.glViewport(0, 0, width, height);
                mImage.onSurfaceChanged(width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                //清楚深度缓存和颜色缓存
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
                mImage.onDrawFrame();
            }
        };
    }
}
