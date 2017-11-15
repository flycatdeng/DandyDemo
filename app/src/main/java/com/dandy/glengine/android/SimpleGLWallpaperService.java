package com.dandy.glengine.android;

import android.content.Context;
import android.view.SurfaceHolder;

import com.dandy.glengine.Stage;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/5/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public abstract class SimpleGLWallpaperService extends GLWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new SimpleGLEngine();
    }

    public abstract ISimpleGLContent getSimpleGLContent(Context context);

    public class SimpleGLEngine extends GLEngine {
        private ISimpleGLContent mContent;

        public SimpleGLEngine() {
            mContent = getSimpleGLContent(mContext);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Stage stage = getStage();
            mContent.onCreate(stage);
        }

        @Override
        protected void onResume() {
            super.onResume();
            mContent.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mContent.onPause();
        }

        @Override
        public void onDestroy() {
            if (mContent != null) {
                mContent.onDestroy();
                mContent = null;
            }
            super.onDestroy();
        }

        @Override
        protected void setStageViewConfigsBeforeSetRender(StageView stageView) {
            super.setStageViewConfigsBeforeSetRender(stageView);
            if (mContent != null) {
                mContent.setStageViewConfigsBeforeSetRender(stageView);
            }
        }
    }
}
