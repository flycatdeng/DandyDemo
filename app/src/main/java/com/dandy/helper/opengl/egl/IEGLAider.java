package com.dandy.helper.opengl.egl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/8
 */
public interface IEGLAider {

    void init(Object nativeWindow);

    void destroyEGLSurface();

    void finish();

    void setPreserveEGLContextOnPause(boolean preserve);

    boolean isPreserveEGLContextOnPause();

    GL getGL();

    EGLConfig getEGLConfig();

    void purgeBuffers();

    boolean swap();
}
