package com.dandy.helper.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * <pre>
 * An interface for customizing the eglCreateWindowSurface and eglDestroySurface calls.
 *
 * This interface must be implemented by clients wishing to call
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/8
 */
public interface EGLWindowSurfaceFactory {
    /**
     * @param egl EGL对象
     * @param display 指定EGL显示连接
     * @param config 指定配置
     * @param nativeWindow 指定原生窗口，
     * @return null if the surface cannot be constructed.
     */
    EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow);

    void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface);
}
