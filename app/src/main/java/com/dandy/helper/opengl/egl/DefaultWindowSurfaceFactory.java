package com.dandy.helper.opengl.egl;

import android.util.Log;

import com.dandy.helper.android.LogHelper;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/8
 */
public class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
    private static final String TAG = "DefaultWindowSurfaceFactory";

    public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
        EGLSurface result = null;
        try {
            result = egl.eglCreateWindowSurface(
                    display, //指定EGL显示连接
                    config,  //指定配置
                    nativeWindow,  //指定原生窗口，
                    null//attribList指定窗口属性列表,EGL_RENDER_BUFFER指定渲染所用的缓冲区，值EGL_SINGLE_BUFFER或EGL_BACK_BUFFER(默认值)
            );
        } catch (IllegalArgumentException e) {
            // This exception indicates that the surface flinger surface
            // is not valid. This can happen if the surface flinger surface has
            // been torn down, but the application has not yet been
            // notified via SurfaceHolder.Callback.surfaceDestroyed.
            // In theory the application should be notified first,
            // but in practice sometimes it is not. See b/4588890
            LogHelper.e(TAG, "eglCreateWindowSurface", e);
        }
        return result;
    }

    public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
        egl.eglDestroySurface(display, surface);
    }
}
