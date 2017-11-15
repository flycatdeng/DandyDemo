package com.dandy.glengine.android;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dandy.glengine.Stage;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.opengl.GLCommonUtils;

/**
 * <pre>
 *     OpenGL引擎 动态壁纸Service，基本框架。
 *     子类实现onCreateEngine，可继承自GLEngine
 * </pre>
 * Created by flycatdeng on 2017/5/8.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public abstract class GLWallpaperService extends WallpaperService {
    protected Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onDestroy() {
        Log.d(LogHelper.getRootTag(), "GLWallpaperService onDestroy");
        mContext = null;
        super.onDestroy();
    }

    /**
     * 实现的是Engine，返回该对象 动态壁纸就可以渲染了
     *
     * @author dengchukun 2016年12月9日
     */
    public class GLEngine extends Engine {
        private ProxyStageView mProxyStageView;

        /**
         * <pre>
         * 开始一直很纠结，为什么会用到这么一个GLSurfaceView，而且没有添加这个View的痕迹，
         * 细心的我终于还是发现了这个天大的秘密，其最主要的作用就是getHolder()方法，里面得到的是mEngine.getSurfaceHolder()
         * 什么意思呢？就是Engine初始化完之后就会有一个SurfaceHolder对象，那只要在这个对象上绘制就行了；
         * 所以呢，GLSurfaceView的作用就是将Engine的SurfaceHolder对象拿到，然后用自身的Render去绘制；
         * 也就是说GLSurfaceView的Render绘制的东西在Engine的SurfaceHolder上
         * </pre>
         * <p>
         * 我开始还纠结另一个问题，就是用户触摸怎么办？岂不是要重新写这个类？后来想了想记得Engine中的onOffsetsChanged已经有这个功效了，也就不用担心了
         * <p style="color:red">
         * 超级邪门，这个类放在外面还不行，硬是要放到OpenGLES2Engine类里边才能有用，否则ProxyGLSurfaceView的TAG都得不到（null）
         * </p>
         *
         * @author dengchukun 2016年12月9日
         */
        class ProxyStageView extends StageView {
            public ProxyStageView(Context context) {
                super(context);
                setStageViewConfigsBeforeSetRender(this);
                initRenderer();
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }
        }//end of ProxyGLSurfaceView

        public Stage getStage() {
            return mProxyStageView.getStage();
        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            boolean isSupportEs2 = GLCommonUtils.isSupportEs2(GLWallpaperService.this);//
            if (!isSupportEs2) {
                throw new RuntimeException("This engine is based on ES2.0,but your device does not support.");
            }
            mProxyStageView = new ProxyStageView(GLWallpaperService.this);
        }

        @Override
        public void onDestroy() {
            Log.d(LogHelper.getRootTag(), "GLEngine onDestroy");
            if (mProxyStageView != null) {
                mProxyStageView.onDestroy();
                mProxyStageView = null;
            }
            super.onDestroy();
            System.gc();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                onResume();
            } else {
                onPause();
            }
        }

        protected void onResume() {
            mProxyStageView.onResume();
        }

        protected void onPause() {
            mProxyStageView.onPause();
        }

        /**
         * 在StageView设置renderer之前设置一些参数，这样以免之后不能设置一些参数了
         *
         * @param stageView
         */
        protected void setStageViewConfigsBeforeSetRender(StageView stageView) {
        }
    }
}
