package com.dandy.helper.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 *
 */
public abstract class BaseConfigChooser
        implements EGLConfigChooser {
    public BaseConfigChooser(int[] configSpec) {
        mConfigSpec = filterConfigSpec(configSpec);
    }

    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int[] numConfig = new int[1];
        //用于获取满足attributes的所有config
        if (!egl.eglChooseConfig(display, //EGLDisplay 指定EGL显示连接
                mConfigSpec,//指定configs匹配的属性列表
                null, //指定配置列表，用于存放输出的configs，这里暂时不用输出，只是查看有多少个合适的
                0,//最多输出多少个EGLConfig
                numConfig //由EGL系统写入，表明满足attributes的config一共有多少个
        )) {
            throw new IllegalArgumentException("eglChooseConfig failed");
        }

        int numConfigs = numConfig[0];

        if (numConfigs <= 0) {
            throw new IllegalArgumentException("No configs match configSpec");
        }

        EGLConfig[] configs = new EGLConfig[numConfigs];
        //和上面的那一步一样，只不过上面那句是为了得到看有多少个符合的EGLConfig,而下面这句是为了得到符合的EGLConfig
        if (!egl.eglChooseConfig(display, //EGLDisplay 指定EGL显示连接
                mConfigSpec, //指定configs匹配的属性列表
                configs,//指定配置列表，用于存放输出的configs
                numConfigs,//最多输出多少个EGLConfig
                numConfig //由EGL系统写入，表明满足attributes的config一共有多少个
        )) {
            throw new IllegalArgumentException("eglChooseConfig#2 failed");
        }
        EGLConfig config = chooseConfig(egl, display, configs);
        if (config == null) {
            throw new IllegalArgumentException("No config chosen");
        }
        return config;
    }

    /**
     * @param egl     egl对象
     * @param display EGL显示连接
     * @param configs 符合传进来的int[] configSpec 的EGLConfig们
     * @return
     */
    abstract EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs);

    protected int[] mConfigSpec;

    private int[] filterConfigSpec(int[] configSpec) {
        /* We know none of the subclasses define EGL_RENDERABLE_TYPE.
        * And we know the configSpec is well formed.
        */
        int len = configSpec.length;
        int[] newConfigSpec = new int[len + 2];
        System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1);
        newConfigSpec[len - 1] = EGL10.EGL_RENDERABLE_TYPE;// 指定渲染api类别,这里或者是硬编码的4，或者是EGL14.EGL_OPENGL_ES2_BIT
        newConfigSpec[len] = 4; /* EGL14.EGL_OPENGL_ES2_BIT，3.0是EGLExt.EGL_OPENGL_ES3_BIT_KHR*/
        newConfigSpec[len + 1] = EGL10.EGL_NONE;// 总是以EGL10.EGL_NONE结尾
        return newConfigSpec;
    }

}
