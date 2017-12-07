package com.dandy.helper.opengl.egl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * <pre>
 *     将通用的EGL的EGLConfigChooser转换成GLSurfaceView里专用的EGLConfigChooser
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/5
 */
public class GLSVConfigChooserConverter {

    public static GLSurfaceView.EGLConfigChooser convertEGLConfigChooser(final EGLConfigChooser eglConfigChooser) {
        return new GLSurfaceView.EGLConfigChooser() {
            @Override
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                return eglConfigChooser.chooseConfig(egl, display);
            }
        };
    }

    public static GLSurfaceView.EGLContextFactory convertContextFactory(final EGLContextFactory eglContextFactory, final int eglContextClientVersion) {
        return new GLSurfaceView.EGLContextFactory() {
            @Override
            public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
                return eglContextFactory.createContext(egl, display, eglConfig, eglContextClientVersion);
            }

            @Override
            public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
                eglContextFactory.destroyContext(egl, display, context);
            }
        };
    }
}
