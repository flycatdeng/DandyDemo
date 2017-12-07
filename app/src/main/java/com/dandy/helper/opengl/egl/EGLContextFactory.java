package com.dandy.helper.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * <pre>
 *     An interface for customizing the eglCreateContext and eglDestroyContext calls.
 *     This interface must be implemented by clients wishing to call
 *     EGLContext包括渲染操作的所有状态信息
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/5
 */
public interface EGLContextFactory {
    /**
     * @param egl                     EGL 对象
     * @param display                 EGL显示连接
     * @param eglConfig               符合渲染要求的EGLConfig
     * @param eglContextClientVersion which EGLContext client version to pick
     * @return
     */
    EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig, int eglContextClientVersion);

    void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context);
}
