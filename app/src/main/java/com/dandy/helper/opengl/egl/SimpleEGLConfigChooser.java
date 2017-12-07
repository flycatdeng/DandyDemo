package com.dandy.helper.opengl.egl;

/**
 * This class will choose a RGB_888 surface with
 * or without a depth buffer.
 * 2017/12/5
 */
public class SimpleEGLConfigChooser extends ComponentSizeChooser {
    public SimpleEGLConfigChooser(boolean withDepthBuffer) {
        super(8, 8, 8, 0, withDepthBuffer ? 16 : 0, 0);
    }
}
