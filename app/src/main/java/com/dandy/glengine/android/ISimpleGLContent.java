package com.dandy.glengine.android;

import com.dandy.glengine.Stage;

/**
 * <pre>
 * Stage 所添加的Actor的生命周期会在Stage中被调用，不用本接口担心，本接口主要是为其他的帮助类提供生命周期
 * </pre>
 * Created by flycatdeng on 2017/5/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public interface ISimpleGLContent {
    void onCreate(Stage stage);

    void onResume();

    void onPause();

    void onDestroy();

    /**
     * 该方法会在{@link #onCreate(Stage)}之前调用，用于设置StageView的一些额外参数
     * 该方法会在StageView设置renderer之前调用，初始化之后调用
     *
     * @param stageView
     */
    void setStageViewConfigsBeforeSetRender(StageView stageView);
}
