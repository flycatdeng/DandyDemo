package com.dandy.glengine.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.opengl.Egl10Aider;
import com.dandy.helper.opengl.GLRenderer;
import com.dandy.helper.opengl.egl.EGLConfigChooser;
import com.dandy.helper.opengl.egl.EGLContextFactory;
import com.dandy.helper.opengl.egl.EGLWindowSurfaceFactory;
import com.dandy.helper.opengl.egl.GLThread;

/**
 * <pre>
 *     调用GLTextureView，一般都是先创建对象，然后设置egl属性，再设置GLRenderer，再被添加到窗口
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/7
 */
public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "GLTextureView";
    private GLRenderer mRenderer;
    private Egl10Aider mEGLAider;
    private GLThread mGLThread;
    private boolean mDetached;
    /**
     * 请求一次才渲染一次，调用{@link #requestRender()}来请求渲染
     */
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    public GLTextureView(Context context) {
        super(context);
        init(context);
    }

    public GLTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setSurfaceTextureListener(this);
        mEGLAider = new Egl10Aider();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mGLThread != null) {
            mGLThread.requestExitAndWait();
        }
        mDetached = true;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDetached && (mRenderer != null)) {

            int renderMode = RENDERMODE_CONTINUOUSLY;
            if (mGLThread != null) {
                renderMode = mGLThread.getRenderMode();
            }
            mGLThread = new GLThread(mRenderer, mEGLAider);
            if (renderMode != RENDERMODE_CONTINUOUSLY) {
                mGLThread.setRenderMode(renderMode);
            }
            mGLThread.start();
        }

        mDetached = false;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            mGLThread.requestRender();
        }
        LogHelper.d(TAG, "onVisibilityChanged, visibility is:" + visibility);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged, w: " + w + " h: " + h + " oldw: " + oldw + " oldh: " + oldh);
        mGLThread.onWindowResize(w, h);
        mGLThread.setSurfaceTextureReady();
        mGLThread.requestRender();
    }

    public void setRenderer(GLRenderer renderer) {
        mEGLAider.checkRenderThreadState();

        mRenderer = renderer;
        mGLThread = new GLThread(mRenderer, mEGLAider);
        mGLThread.start();
        mEGLAider.setGLThreadCreated(true);//must call after GL Thread start
    }

    /**
     * Queue a runnable to be run on the GL rendering thread. This can be used
     * to communicate with the Renderer on the rendering thread.
     * Must not be called before a renderer has been set.
     *
     * @param r the runnable to be run on the GL rendering thread.
     */
    public void queueEvent(Runnable r) {
        mGLThread.queueEvent(r);
    }

    public void runInGLThread(Runnable runnable) {
        if (Thread.currentThread() == mGLThread) {
            runnable.run();
        } else {
            queueEvent(runnable);
        }
    }

    /**
     * Install a custom EGLWindowSurfaceFactory.
     * <p>If this method is
     * called, it must be called before {@link #setRenderer(GLRenderer)}
     * is called.
     * <p>
     * If this method is not called, then by default
     * a window surface will be created with a null attribute list.
     */
    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        mEGLAider.setEGLWindowSurfaceFactory(factory);
    }

    /**
     * 在{@link #setRenderer(GLRenderer)}之前调用，否则会报错
     *
     * @param configChooser 符合渲染需求的EGLConfig参数设置选择
     */
    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        mEGLAider.setEGLConfigChooser(configChooser);
    }

    /**
     * 在{@link #setRenderer(GLRenderer)}之前调用，否则会报错
     *
     * @param factory EGL环境上下文
     */
    public void setEGLContextFactory(EGLContextFactory factory) {
        mEGLAider.setEGLContextFactory(factory);
    }

    /**
     * 在{@link #setRenderer(GLRenderer)}之前调用，否则会报错
     *
     * @param version 设置当前使用gl几点几 例如2：2.0 3：3.0
     */
    public void setEGLContextClientVersion(int version) {
        mEGLAider.setEGLContextClientVersion(version);
    }
    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        mEGLAider.setPreserveEGLContextOnPause(preserveOnPause);
    }
    /**
     * <pre>
     * 类似于GLSurfaceView的setRenderMode
     * 渲染模式，是循环刷新，还是请求的时候刷新
     * </pre>
     */
    /**
     * Set the rendering mode. When renderMode is
     * RENDERMODE_CONTINUOUSLY, the renderer is called
     * repeatedly to re-render the scene. When renderMode
     * is RENDERMODE_WHEN_DIRTY, the renderer only rendered when the surface
     * is created, or when {@link #requestRender} is called. Defaults to RENDERMODE_CONTINUOUSLY.
     * <p>
     * Using RENDERMODE_WHEN_DIRTY can improve battery life and overall system performance
     * by allowing the GPU and CPU to idle when the view does not need to be updated.
     * <p>
     *
     * @param renderMode one of the RENDERMODE_X constants
     * @see #RENDERMODE_CONTINUOUSLY
     * @see #RENDERMODE_WHEN_DIRTY
     */
    public void setRenderMode(int renderMode) {
        mGLThread.setRenderMode(renderMode);
    }

    /**
     * Get the current rendering mode. May be called
     * from any thread. Must not be called before a renderer has been set.
     *
     * @return the current rendering mode.
     * @see #RENDERMODE_CONTINUOUSLY
     * @see #RENDERMODE_WHEN_DIRTY
     */
    public int getRenderMode() {
        return mGLThread.getRenderMode();
    }

    /**
     * Request that the renderer render a frame. This method is typically used when the render mode has been set to {@link #RENDERMODE_WHEN_DIRTY}, so
     * that frames are only rendered on demand. May be called from any thread. Must not be called before a renderer has been set.
     */
    public void requestRender() {
        mGLThread.requestRender();
    }

    public void onResume() {
        if (mGLThread != null) {
            mGLThread.onResume();
        }
    }

    public void onPause() {
        if (mGLThread != null) {
            mGLThread.onPause();
        }
    }

    public void onDestroy() {
        if (mGLThread != null) {
            mGLThread.onDestroy();
            mGLThread = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // Make view be transparent to fix black background issue in JB
        makeTransparency();

        mGLThread.surfaceCreated(surface, width, height);
        // Handle Low Memory case: Make sure GLthread is resume after surface texture available
        mGLThread.onResume();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // Make view be transparent to fix black background issue in JB
        makeTransparency();

        // Handle Low Memory case: Make sure GLthread is paused before destroy surface
        mGLThread.onPause();
        mGLThread.surfaceDestroyed();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        mGLThread.setSurfaceTextureReady();
    }

    protected void makeTransparency() {
        setAlpha(0);
    }
}
