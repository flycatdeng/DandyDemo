package com.dandy.helper.opengl.egl;

import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.SurfaceTexture;
import android.util.Log;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.ReflectionHelper;
import com.dandy.helper.opengl.GLRenderer;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/7
 */
public class GLThread extends Thread {
    private static final String TAG = "EGLThread";
    private static final boolean LOG_DEBUG = true;
    /**
     * 请求一次才渲染一次，调用{@link #requestRender()}来请求渲染
     */
    private final static int RENDERMODE_WHEN_DIRTY = 0;
    private final static int RENDERMODE_CONTINUOUSLY = 1;
    private static final boolean DRAW_TWICE_AFTER_SIZE_CHANGED = true;
    private IEGLAider mEGLAider;
    private GLRenderer mRenderer;
    private Object mNativeWindow = null;
    private int mRenderMode = RENDERMODE_CONTINUOUSLY;
    private boolean mRequestRender;
    // Sync with SurfaceTexture
    private boolean mSurfaceTextureReady;
    private boolean mHaveEglContext;//是否有EGLContext,当需要退出时置为false
    private boolean mHaveEglSurface;
    private boolean mSizeChanged = true;//size变换是否已经变更（默认时 0 0）
    private boolean mShouldReleaseEglContext;//是否需要释放EGLContext
    private boolean mHasSurface;//native window 对象是否已经有了？
    private boolean mSurfaceIsBad;
    private boolean mWaitingForSurface;
    private boolean mShouldExit;//是否请求退出渲染
    private boolean mExited;//线程状态是否为：已经退出了
    private boolean mRequestPaused;//是否请求暂停渲染
    private boolean mPaused;//是否是不可见的
    private boolean mRenderComplete;
    private final ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();//PendingThreadAider的作用
    //NativeWindow宽高
    private int mWidth;
    private int mHeight;

    public GLThread(GLRenderer renderer, IEGLAider eglAider) {
        super();
        this.mRenderer = renderer;
        this.mEGLAider = eglAider;
        mRequestRender = true;
        mSurfaceTextureReady = true;
        mRenderMode = RENDERMODE_CONTINUOUSLY;
    }

    @Override
    public void run() {
        setName("GLThread-" + getId());
        try {
            guardedRun();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //TODO
//            GL_THREAD_MANAGER.threadExiting(this);
        }
    }

    private void guardedRun() throws InterruptedException {
        mHaveEglContext = false;
        mHaveEglSurface = false;
        try {
            boolean askedToReleaseEglContext = false;
            boolean lostEglContext = false;
            boolean doRenderNotification = false;
            boolean wantRenderNotification = false;
            GL10 gl = null;//用于renderer参数的返回
            boolean createEglContext = false;//用于renderer的回调标志
            boolean sizeChanged = false;//用于renderer的回调标志
            Runnable event = null;//从队列中去遍历add进来的要被执行一次的runnable
            while (true) {//死循环跑
                synchronized (GL_THREAD_MANAGER) {
                    while (true) {
                        if (mShouldExit) {//需要退出,跳出第一层循环，直接退出这个方法体了
                            return;
                        }
                        if (!mEventQueue.isEmpty()) {//如果队列中有事件，则取出一个来，跳出第二层循环，去处理这个event
                            event = mEventQueue.remove(0);
                            break;
                        }
                        // Update the pause state.
                        boolean pausing = false;
                        if (mPaused != mRequestPaused) {//一开始否是false,直到调用onPause后mRequestPaused被指为true
                            mPaused = mRequestPaused;
                            GL_THREAD_MANAGER.notifyAll();
                            if (LOG_DEBUG) {
                                LogHelper.d(TAG, "mPaused is now " + mPaused + " tid=" + getId());
                            }
                        }

                        // Do we need to give up the EGL context?
                        if (mShouldReleaseEglContext) {//mHaveEglContext为true时会请求释放
                            stopEglSurfaceLocked();//销毁EGLSurface
                            stopEglContextLocked();//销毁EGLContext
                            mShouldReleaseEglContext = false;
                            askedToReleaseEglContext = true;
                        }

                        // Have we lost the EGL context?
                        if (lostEglContext) {
                            stopEglSurfaceLocked();//销毁EGLSurface
                            stopEglContextLocked();//销毁EGLContext
                            lostEglContext = false;
                        }

                        // When pausing, release the EGL surface: 释放EGLSurface
                        if (pausing && mHaveEglSurface) {
                            stopEglSurfaceLocked();
                        }
                        // When pausing, optionally release the EGL Context:当onPause的时候是否保存EGLContext
                        if (pausing && mHaveEglContext) {
                            boolean preserveEglContextOnPause = mEGLAider.isPreserveEGLContextOnPause();
                            if (!preserveEglContextOnPause || GL_THREAD_MANAGER.shouldReleaseEGLContextWhenPausing()) {
                                stopEglContextLocked();
                            }
                        }

                        // When pausing, optionally terminate EGL:当onPause的时候是否终结EGL
                        if (pausing) {
                            if (GL_THREAD_MANAGER.shouldTerminateEGLWhenPausing()) {
                                mEGLAider.finish();
                            }
                        }

                        // Have we lost the NativeWindow? 没有NativeWindow的对象？
                        if ((!mHasSurface) && (!mWaitingForSurface)) {
                            if (mHaveEglSurface) {
                                stopEglSurfaceLocked();
                            }
                            mWaitingForSurface = true;//没有那就等啊
                            mSurfaceIsBad = false;
                            // Notify the wait in surfaceDestroyed() to avoid ANR
                            // with low memory case, Activity Manager destroys hardware resource of SurfaceTexture directly.
                            GL_THREAD_MANAGER.notifyAll();
                        }
                        // Have we acquired the NativeWindow? 已经有NativeWindow了
                        if (mHasSurface && mWaitingForSurface) {
                            mWaitingForSurface = false;
                            GL_THREAD_MANAGER.notifyAll();
                        }

                        if (doRenderNotification) {
                            wantRenderNotification = false;
                            doRenderNotification = false;
                            mRenderComplete = true;
                            GL_THREAD_MANAGER.notifyAll();
                        }
                        // Ready to draw? 可以开始渲染？
                        if (readyToDraw()) {
                            // If we don't have an EGL context, try to acquire one.
                            if (!mHaveEglContext) {
                                if (askedToReleaseEglContext) {
                                    askedToReleaseEglContext = false;
                                } else if (GL_THREAD_MANAGER.tryAcquireEglContextLocked(this)) {
                                    try {
                                        mEGLAider.init(mNativeWindow);//初始化EGL环境，上下文，EGLSurface等
                                    } catch (RuntimeException t) {
                                        GL_THREAD_MANAGER.releaseEglContextLocked(this);
                                        throw t;
                                    }
                                    mHaveEglContext = true;
                                    createEglContext = true;

                                    GL_THREAD_MANAGER.notifyAll();
                                }
                            }
                            if (mHaveEglContext && !mHaveEglSurface) {
                                mHaveEglSurface = true;
                                sizeChanged = true;
                            }
                            if (mHaveEglSurface) {//EGL环境都创建好了，EGLSurface也有了
                                if (mSizeChanged) {//宽高不是00，只被赋值一次
                                    sizeChanged = true;
                                    wantRenderNotification = true;
                                    if (DRAW_TWICE_AFTER_SIZE_CHANGED) {
                                        // We keep mRequestRender true so that we draw twice after the size changes.
                                        // (Once because of mSizeChanged, the second time because of mRequestRender.)
                                        // This forces the updated graphics onto the screen.
                                    } else {
                                        mRequestRender = false;
                                        mSurfaceTextureReady = false;
                                    }
                                    mSizeChanged = false;
                                } else {
                                    mRequestRender = false;
                                    mSurfaceTextureReady = false;
                                }
                                GL_THREAD_MANAGER.notifyAll();
                                break;
                            }

                        }
                        // By design, this is the only place in a GLThread thread where we wait().
                        //没有要处理的消息就会一直等待
                        GL_THREAD_MANAGER.wait();
                    }//end of 内层while循环，
                }//end of synchronized (GL_THREAD_MANAGER)
                if (event != null) {//把所有的event都取出来执行完
                    event.run();
                    event = null;
                    continue;
                }
                if (!mHasSurface) {//如果还没有界面，则继续循环，
                    continue;
                }
                if (createEglContext) {//surface创建好了onSurfaceCreated的回调。
                    gl = (GL10) mEGLAider.getGL();
                    mRenderer.onSurfaceCreated(gl, mEGLAider.getEGLConfig());
                    createEglContext = false;
                }
                if (sizeChanged) {//surface onSurfaceChanged回调
                    mEGLAider.purgeBuffers();
                    mRenderer.onSurfaceChanged(gl, mWidth, mHeight);
                    sizeChanged = false;
                }
                mRenderer.onDrawFrame(gl);//每一帧绘制回调
                if (!mEGLAider.swap()) {//将绘制好的帧缓冲区显示出来
                    lostEglContext = true;
                }
                if (wantRenderNotification) {
                    doRenderNotification = true;
                }

            }//end of while
        } finally {
//TODO
        }
    }

    private boolean readyToDraw() {
        return (!mPaused) //见面可见
                && mHasSurface //有NativeWindow
                && (!mSurfaceIsBad)
                && (mWidth > 0) && (mHeight > 0) //界面宽高大于0
                && (mRequestRender || (mRenderMode == RENDERMODE_CONTINUOUSLY)) //请求渲染了，或者时模式为循环渲染
//                && mSurfaceTextureReady;
                ;
    }

    /*
    * 销毁EGLSurface
    * This private method should only be called inside a
    * synchronized(GL_THREAD_MANAGER) block.
    */
    private void stopEglSurfaceLocked() {
        if (mHaveEglSurface) {
            mHaveEglSurface = false;
            mEGLAider.destroyEGLSurface();
        }
    }

    /*
     * 销毁EGLContext
     * This private method should only be called inside a
     * synchronized(GL_THREAD_MANAGER) block.
     */
    private void stopEglContextLocked() {
        if (mHaveEglContext) {
            mEGLAider.finish();
            mHaveEglContext = false;
            //TODO
//            GL_THREAD_MANAGER.releaseEglContextLocked(this);
        }
    }

    private void requestReleaseEglContextLocked() {
        mShouldReleaseEglContext = true;//
        GL_THREAD_MANAGER.notifyAll();
    }

    public void requestExitAndWait() {
    }


    public void requestRender() {
        synchronized (GL_THREAD_MANAGER) {
            mRequestRender = true;
            GL_THREAD_MANAGER.notifyAll();
        }
    }

    public int getRenderMode() {
        synchronized (GL_THREAD_MANAGER) {
            return mRenderMode;
        }
    }

    public void setRenderMode(int renderMode) {
        if (!((RENDERMODE_WHEN_DIRTY <= renderMode) && (renderMode <= RENDERMODE_CONTINUOUSLY))) {//renderMode必须是两者中的一个
            throw new IllegalArgumentException("renderMode");
        }
        synchronized (GL_THREAD_MANAGER) {
            mRenderMode = renderMode;
            GL_THREAD_MANAGER.notifyAll();
        }
    }

    public void queueEvent(Runnable r) {
    }

    public void onResume() {
    }

    public void onPause() {
        synchronized (GL_THREAD_MANAGER) {
            mSurfaceTextureReady = true;
            mRequestPaused = true;//请求暂停渲染
            GL_THREAD_MANAGER.notifyAll();
            while ((!mExited) && (!mPaused)) {
                try {
                    GL_THREAD_MANAGER.wait();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void onDestroy() {
    }

    public void surfaceDestroyed() {
    }

    public void setSurfaceTextureReady() {
    }

    public void surfaceCreated(SurfaceTexture surface, int width, int height) {
    }

    public void onWindowResize(int w, int h) {
    }

    private final GLThreadManager GL_THREAD_MANAGER = new GLThreadManager();

    private class GLThreadManager {
        private static final String TAG = "GLThreadManager";
        private boolean mGLESVersionCheckComplete;
        private int mGLESVersion;
        private boolean mLimitedGLESContexts = true;
        private boolean mMultipleGLESContextsAllowed = false;
        private GLThread mEglOwner;

        public boolean shouldReleaseEGLContextWhenPausing() {
            synchronized (this) {
                return mLimitedGLESContexts;
            }
        }

        public boolean shouldTerminateEGLWhenPausing() {
            synchronized (this) {
                int version = (Integer) ReflectionHelper.getStaticMethodReturnObject(
                        "android.os.SystemProperties", "getInt", new Class[]{String.class, int.class},
                        new Object[]{"ro.opengles.version", ConfigurationInfo.GL_ES_VERSION_UNDEFINED});
                LogHelper.d(TAG, LogHelper.getThreadName() + " version=" + version);
                if (version >= 0x20000) {//>=2
                    return true;
                }
                return false;
            }
        }

        /*
         * Tries once to acquire the right to use an EGL
         * context. Does not block. Requires that we are already
         * in the GL_THREAD_MANAGER monitor when this is called.
         *
         * @return true if the right to use an EGL context was acquired.
         */
        public boolean tryAcquireEglContextLocked(GLThread glThread) {
            if (mEglOwner == glThread || mEglOwner == null) {
                mEglOwner = glThread;
                notifyAll();
                return true;
            }
            checkGLESVersion();
            if (mMultipleGLESContextsAllowed) {
                return true;
            }
            // Notify the owning thread that it should release the context.
            // TODO: implement a fairness policy. Currently
            // if the owning thread is drawing continuously it will just
            // reacquire the EGL context.
            if (mEglOwner != null) {
                mEglOwner.requestReleaseEglContextLocked();
            }
            return false;
        }

        private void checkGLESVersion() {
            if (!mGLESVersionCheckComplete) {
                mGLESVersion = (Integer) ReflectionHelper.getStaticMethodReturnObject(
                        "android.os.SystemProperties", "getInt", new Class[]{String.class, int.class},
                        new Object[]{"ro.opengles.version", ConfigurationInfo.GL_ES_VERSION_UNDEFINED});
                LogHelper.d(TAG, LogHelper.getThreadName() + " mGLESVersion=" + mGLESVersion);
                if (mGLESVersion >= 0x20000) {
                    mMultipleGLESContextsAllowed = true;
                }
                mGLESVersionCheckComplete = true;
            }
        }

        /*
         * Releases the EGL context. Requires that we are already in the
         * GL_THREAD_MANAGER monitor when this is called.
         */
        public void releaseEglContextLocked(GLThread thread) {
            if (mEglOwner == thread) {
                mEglOwner = null;
            }
            notifyAll();
        }
    }
}
