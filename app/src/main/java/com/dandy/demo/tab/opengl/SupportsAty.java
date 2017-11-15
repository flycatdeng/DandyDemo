package com.dandy.demo.tab.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.opengl.GLCommonUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <pre>
 *     查看某些属性是否支持
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class SupportsAty extends BaseDemoAty {
    private static final String TAG = "SupportsAty";
    private Context mContext;
    private LinearLayout mContentLayout;
    private MyGLSurfaceView mMyGLSurfaceView;
    private GL10 mGL10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        mMyGLSurfaceView = new MyGLSurfaceView(mContext);
        FrameLayout parentLayout = new FrameLayout(mContext);
        parentLayout.addView(mMyGLSurfaceView);
//        mMyGLSurfaceView.setVisibility(View.INVISIBLE);
//        parentLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(mContext);
        mContentLayout = new LinearLayout(mContext);
        mContentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.addView(scrollView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(mContentLayout);
        setContentView(parentLayout);
    }

    //有些参数需要借助GL10对象才能获得，所以这里是在添加一个GLSurfaceView成功之后再设置的内容
    private void setContents() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        checkSupportEs20();//是否支持ES2.0
        checkSupportEs30();//是否支持ES3.0
        checkGL10Object();
        checkSupportFBO();
    }

    private void checkGL10Object() {
        String value = mGL10 == null ? "null" : "hasValue";
        setContentItem("GL10", value);
    }

    private void checkSupportFBO() {
        String value = GLCommonUtils.checkIfContextSupportsFrameBufferObject(mGL10) + "";
        setContentItem("GL_OES_framebuffer_object", value);
    }

    private void checkSupportEs30() {
        String value = GLCommonUtils.isSupportEs3(mContext) + "";
        setContentItem("ES3.0", value);
    }

    private void checkSupportEs20() {
        String value = GLCommonUtils.isSupportEs2(mContext) + "";
        setContentItem("ES2.0", value);
    }

    private void setContentItem(String key, String value) {
        TextView tv = new TextView(mContext);
        tv.setText(key + ":" + value);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextSize(30);
        mContentLayout.addView(tv);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        public MyGLSurfaceView(Context context) {
            super(context);
            setRenderer(new Renderer() {
                @Override
                public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                    gl.glClearColor(1, 1, 1, 1);
                    LogHelper.d(TAG, LogHelper.getThreadName());
                    mGL10 = gl;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContents();
                        }
                    });
                }

                @Override
                public void onSurfaceChanged(GL10 gl, int width, int height) {

                }

                @Override
                public void onDrawFrame(GL10 gl) {

                }
            });
            setRenderMode(RENDERMODE_WHEN_DIRTY);
        }
    }
}
