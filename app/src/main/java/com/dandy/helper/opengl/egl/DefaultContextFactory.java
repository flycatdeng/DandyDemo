package com.dandy.helper.opengl.egl;

import com.dandy.helper.android.LogHelper;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/12/5
 */
public class DefaultContextFactory implements EGLContextFactory {
    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

    /**
     * @param egl                     EGL 对象
     * @param display                 EGL显示连接
     * @param config                  符合渲染要求的EGLConfig
     * @param eglContextClientVersion which EGLContext client version to pick
     * @return
     */
    public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config, int eglContextClientVersion) {
        int[] attribList = {EGL_CONTEXT_CLIENT_VERSION, //attrib_list,目前可用属性只有EGL_CONTEXT_CLIENT_VERSION,在Android4.2之前，没有EGL_CONTEXT_CLIENT_VERSION这个属性，只能使用硬编码0x3098代替
                eglContextClientVersion,//1代表OpenGL ES 1.x,2代表2.0。
                EGL10.EGL_NONE //属性列表结尾
        };
        // 创建context
        return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT,// share_context,是否有context共享，共享的contxt之间亦共享所有数据。EGL_NO_CONTEXT代表不共享
                eglContextClientVersion == 0 ? null : attribList);
    }

    public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
        if (!egl.eglDestroyContext(display, context)) {
            LogHelper.d("DefaultContextFactory", "display:" + display + " context: " + context);
            throw new RuntimeException("eglDestroyContext failed: ");
        }
    }
}
