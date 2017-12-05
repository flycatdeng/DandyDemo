package com.dandy.helper.opengl.eglconfigchooser;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * 从GLSurfaceView里移植过来的
 * Choose a configuration with exactly the specified r,g,b,a sizes,
 * and at least the specified depth and stencil sizes.
 */
public class ComponentSizeChooser extends BaseConfigChooser {
    /**
     * @param redSize     颜色缓冲区中 红色 分量位数
     * @param greenSize   颜色缓冲区中 绿色 分量位数
     * @param blueSize    颜色缓冲区中 蓝色 分量位数
     * @param alphaSize   颜色缓冲区中 Alpha值 分量位数
     * @param depthSize   深度缓冲区位数
     * @param stencilSize 模板缓冲区位数
     */
    public ComponentSizeChooser(int redSize, int greenSize, int blueSize,
                                int alphaSize, int depthSize, int stencilSize) {
        super(new int[]{
                EGL10.EGL_RED_SIZE, redSize,
                EGL10.EGL_GREEN_SIZE, greenSize,
                EGL10.EGL_BLUE_SIZE, blueSize,
                EGL10.EGL_ALPHA_SIZE, alphaSize,
                EGL10.EGL_DEPTH_SIZE, depthSize,
                EGL10.EGL_STENCIL_SIZE, stencilSize,
                EGL10.EGL_NONE});
        mValue = new int[1];
        mRedSize = redSize;
        mGreenSize = greenSize;
        mBlueSize = blueSize;
        mAlphaSize = alphaSize;
        mDepthSize = depthSize;
        mStencilSize = stencilSize;
    }

    @Override
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,EGLConfig[] configs) {
        for (EGLConfig config : configs) {
            int d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);//查找与EGLConfig相关的 深度缓冲区位数 属性
            int s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);//查找与EGLConfig相关的 模版缓冲区位数 属性
            if ((d >= mDepthSize) && (s >= mStencilSize)) {
                int r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
                int g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
                int b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
                int a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);
                if ((r == mRedSize) && (g == mGreenSize) && (b == mBlueSize) && (a == mAlphaSize)) {
                    return config;
                }
            }
        }
        return null;
    }

    /**
     * 查找与EGLConfig相关的特定属性，成功则返回指定返回值，失败则返回默认值
     *
     * @param egl
     * @param display
     * @param config
     * @param attribute
     * @param defaultValue
     * @return
     */
    private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
        if (egl.eglGetConfigAttrib(//查询与EGLConfig相关的特定属性。成功ture,失败false,如果attribute不是有效的属性则返回EGL_BAD_ATTRIBUTE
                display, //EGL显示连接
                config, //要查询的配置
                attribute,//指定返回的特定属性
                mValue //指定返回值
        )) {
            return mValue[0];
        }
        return defaultValue;
    }

    private final int[] mValue;
    // Subclasses can adjust these values:
    protected int mRedSize;
    protected int mGreenSize;
    protected int mBlueSize;
    protected int mAlphaSize;
    protected int mDepthSize;
    protected int mStencilSize;
}