package com.dandy.helper.opengl;

import android.opengl.GLUtils;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.opengl.egl.DefaultContextFactory;
import com.dandy.helper.opengl.egl.DefaultWindowSurfaceFactory;
import com.dandy.helper.opengl.egl.EGLConfigChooser;
import com.dandy.helper.opengl.egl.EGLContextFactory;
import com.dandy.helper.opengl.egl.EGLWindowSurfaceFactory;
import com.dandy.helper.opengl.egl.IEGLAider;
import com.dandy.helper.opengl.egl.SimpleEGLConfigChooser;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

/**
 * <pre>
 *     EGL提供如下机制：
 *          1.与设备的原生窗口系统通信。Android里则是Surface
 *          2.查询绘图表面的可用类型和配置
 *          3.创建绘图表面
 *          4.在opengl es 3.0和其他图形渲染API之间同步渲染
 *          5.管理纹理贴图等渲染资源
 * </pre>
 * <pre>
 *     双缓冲：
 *        原因：屏幕上可见的帧缓冲区由一个像素数据的二维数组表示。我们可以将在屏幕上显示的图像视为在绘制时简单的更新可见帧缓冲区中的像素数据。
 *              但是，直接在可显示缓冲区上更新像素有一个严重的问题——物理屏幕以固定的从帧缓冲区内存中更新。如果我们直接绘制到帧缓冲区，那么
 *              用户在部分更新帧缓冲区时会看到伪像。
 *        解决：双缓冲区-前缓冲区和后缓冲区。所有渲染都发生在后台缓冲区，他位于不可见于屏幕的内存区域。当所有渲染完成时，这个缓冲区被交换到前缓冲区（可见缓冲区）。
 *              然后前台缓冲区变成下一个后台缓冲区
 *      GL里使用eglSwapBuffers(	EGLDisplay display,EGLSurface surface)控制前后台缓冲区的交换。
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/5
 */
public class Egl10Aider implements IEGLAider {
    private static final String TAG = "Egl10Aider";
    private EGL10 mEgl;
    private EGLDisplay mEglDisplay = EGL10.EGL_NO_DISPLAY;// 显示设备
    private EGLConfig mEglConfig;//符合渲染需求的EGLConfig
    private EGLContext mEglContext = EGL10.EGL_NO_CONTEXT;
    private EGLSurface mEglSurface = EGL10.EGL_NO_SURFACE;
    private EGLConfigChooser mEGLConfigChooser;//符合渲染需求的EGLConfig选择帮助类
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private boolean mIsGLThreadCreated = false;
    private int mEGLContextClientVersion;
    private boolean mPreserveEGLContextOnPause;//onPause的时候是否保存当前EGL上下文

    /**
     * @param nativeWindowSurface NativeWindowType类型，从c层源码可以看到，Android里可以是SurfaceView或SurfaceHolder或Surface，前两者都是为了提供Surface。或者是SurfaceTexture
     */
    public void init(Object nativeWindowSurface) {
        //1.获取EGL对象
        mEgl = (EGL10) EGLContext.getEGL();

        //2.与窗口系统通信
        mEglDisplay = mEgl.eglGetDisplay(// 获取显示设备,打开与显示服务器的连接
                EGL10.EGL_DEFAULT_DISPLAY //NativeDisplayType 指定显示连接，默认为EGL_DEFAULT_DISPLAY
        );//https://www.khronos.org/registry/EGL/sdk/docs/man/html/eglGetDisplay.xhtml
        if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {//返回EGL_NO_DISPLAY则表示显示连接不可用
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
        }//否则表示成功打开连接

        //3.初始化EGL
        int[] version = new int[2];
        if (!mEgl.eglInitialize(mEglDisplay, version)) {// //version中存放EGL 版本号，int[0]为主版本号，int[1]为子版本号
            //EGL_BAD_DISPLAY 如果mEglDisplay没有指定有效的EGLDisplay
            //EGL_NOT_INITIALIZED 如果EGL不能初始化
            throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
        }

        //4.得到符合渲染需求的EGLConfig
        if (mEGLConfigChooser == null) {
            mEGLConfigChooser = new SimpleEGLConfigChooser(true);
        }
        mEglConfig = mEGLConfigChooser.chooseConfig(mEgl, mEglDisplay);//实现类中调用egl.eglChooseConfig
        if (mEglConfig == null) {
            throw new RuntimeException("mEglConfig not initialized");
        }

        //5.获得EGL上下文环境
        if (mEGLContextFactory == null) {
            mEGLContextFactory = new DefaultContextFactory();
        }
        mEglContext = mEGLContextFactory.createContext(mEgl, mEglDisplay, mEglConfig, mEGLContextClientVersion);
        if (mEglContext == null || mEglContext == EGL10.EGL_NO_CONTEXT) {
            mEglContext = null;
            throw new RuntimeException("createContext failed : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
        }

        //6.创建屏幕上的渲染区域：EGL窗口
        if (mEGLWindowSurfaceFactory == null) {
            mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        mEglSurface = mEGLWindowSurfaceFactory.createWindowSurface(mEgl, mEglDisplay, mEglConfig, nativeWindowSurface);
        if (mEglSurface == null || mEglSurface == EGL10.EGL_NO_SURFACE) {
            int error = mEgl.eglGetError();
            //EGL_BAD_MATCH 12297 nativeWindowSurface不匹配提供的mEglConfig
            //EGL_BAD_CONFIG 12293 mEglConfig没有得到系统的支持
            //EGL_BAD_ALLOC 12291 无法为新的EGL窗口分配资源，或者已经有和提供的原生窗口关联的EGLConfig
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {//12299原生窗口提供的句柄无效
                LogHelper.d(TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            throw new RuntimeException("createWindowSurface failed " + GLUtils.getEGLErrorString(error));
        }
        //至此，以上代码就创建了一个绘图场所。窗口并不是唯一的可用渲染表面，EGL Pbuffer也能创建渲染区域

        //7.完成最后一步才能开始渲染：指定某个EGLContext为当前上下文
        boolean eglMakeCurrent = mEgl.eglMakeCurrent(
                mEglDisplay, //指定EGL显示连接
                mEglSurface, //draw 指定EGL绘图表面
                mEglSurface, //read 指定EGL读取表面
                mEglContext //指定连接到该表面的渲染上下文
        );
        if (!eglMakeCurrent) {// 设置为当前的渲染环境
            throw new RuntimeException("eglMakeCurrent failed : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
        }
    }

    public void show(EGLDisplay display, EGLSurface surface) {
        mEgl.eglSwapBuffers(display, surface);
    }

    @Override
    public boolean swap() {
        show(mEglDisplay, mEglSurface);
        return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
    }

    public void onDestroy() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mEglContext != null) {
            mEGLContextFactory.destroyContext(mEgl, mEglDisplay, mEglContext);
            mEglContext = null;
        }
        if (mEglDisplay != null) {
            mEgl.eglTerminate(mEglDisplay);
            mEglDisplay = null;
        }
    }

    public void destroySurface() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mEglSurface != null && mEglSurface != EGL10.EGL_NO_SURFACE) {
            mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_CONTEXT);
            mEGLWindowSurfaceFactory.destroySurface(mEgl, mEglDisplay, mEglSurface);
            mEglSurface = null;
        }
    }

    public void purgeBuffers() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mEgl.eglMakeCurrent(mEglDisplay,
                EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        mEgl.eglMakeCurrent(mEglDisplay,
                mEglSurface, mEglSurface,
                mEglContext);
    }

    public void setGLThreadCreated(boolean created) {
        mIsGLThreadCreated = created;
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        checkRenderThreadState();
        mEGLConfigChooser = configChooser;
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        checkRenderThreadState();
        mEGLContextFactory = factory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory eglWindowSurfaceFactory) {
        checkRenderThreadState();
        mEGLWindowSurfaceFactory = eglWindowSurfaceFactory;
    }

    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        mPreserveEGLContextOnPause = preserveOnPause;
    }

    public boolean isPreserveEGLContextOnPause() {
        return mPreserveEGLContextOnPause;
    }

    @Override
    public GL getGL() {
        return mEglContext.getGL();
    }

    @Override
    public EGLConfig getEGLConfig() {
        return mEglConfig;
    }

    /**
     * Inform the default EGLContextFactory and default EGLConfigChooser
     * which EGLContext client version to pick.
     * <p>Use this method to create an OpenGL ES 2.0-compatible context.
     */
    public void setEGLContextClientVersion(int version) {
        checkRenderThreadState();
        mEGLContextClientVersion = version;
    }

    public void checkRenderThreadState() {
        if (mIsGLThreadCreated) {
            throw new IllegalStateException(
                    "setRenderer has already been called for this instance.");
        }
    }

    @Override
    public void destroyEGLSurface() {

    }

    @Override
    public void finish() {

    }
}
