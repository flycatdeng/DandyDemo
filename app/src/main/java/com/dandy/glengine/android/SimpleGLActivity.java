package com.dandy.glengine.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.dandy.glengine.Stage;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */
public abstract class SimpleGLActivity extends Activity {
    private ISimpleGLContent mContent;
    private StageView mStageView;

    public abstract ISimpleGLContent getSimpleGLContent(Context context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getSimpleGLContent(this);
        mStageView = new StageView(this);
        mContent.setStageViewConfigsBeforeSetRender(mStageView);//在StageView设置renderer之前设置一些参数，这样以免之后不能设置一些参数了
        mStageView.initRenderer();
        setContentView(mStageView);
        Stage stage = mStageView.getStage();
        mContent.onCreate(stage);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStageView != null) {
            mStageView.onResume();
        }
        mContent.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStageView != null) {
            mStageView.onPause();
        }
        mContent.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mContent != null) {
            mContent.onDestroy();
            mContent = null;
        }
        if (mStageView != null) {
            mStageView.onDestroy();
            mStageView = null;
        }
        super.onDestroy();
    }

}
