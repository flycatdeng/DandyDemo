package com.dandy.helper.opengl.eglconfigchooser;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
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

    public static GLSurfaceView.EGLConfigChooser convert(final EGLConfigChooser eglConfigChooser) {
        return new GLSurfaceView.EGLConfigChooser() {
            @Override
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                return eglConfigChooser.chooseConfig(egl, display);
            }
        };
    }
}
