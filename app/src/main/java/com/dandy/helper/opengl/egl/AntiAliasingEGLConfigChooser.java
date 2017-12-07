package com.dandy.helper.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * <pre>
 *  抗锯齿的EGLConfigChooser类，用于OpenGL中加载时抗锯齿
 *  必须在setRenderer之前调用setEGLConfigChooser
 * </pre>
 * Created by flycatdeng on 2017/4/11.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */
public class AntiAliasingEGLConfigChooser implements EGLConfigChooser {
    @Override
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int attribs[] = {
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,  // 在这里修改MSAA的倍数，4就是4xMSAA，再往上开程序可能会崩
                EGL10.EGL_RENDERABLE_TYPE, 4,  // 指定渲染api类别,这里或者是硬编码的4，或者是EGL14.EGL_OPENGL_ES2_BIT，3.0是EGLExt.EGL_OPENGL_ES3_BIT_KHR
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] configCounts = new int[1];
        egl.eglChooseConfig(display, //EGLDisplay 指定EGL显示连接
                attribs,  //指定configs匹配的属性列表
                configs,//指定配置列表，用于存放输出的configs
                1, //最多输出多少个EGLConfig
                configCounts //由EGL系统写入，表明满足attributes的config一共有多少个
        );

        if (configCounts[0] == 0) {
            // Failed! Error handling.
            return null;
        } else {
            return configs[0];
        }
    }
}
