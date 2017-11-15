package com.dandy.module.obj3dload;

/**
 * <pre>
 *      加载成功与否
 *      成功了则将Obj3DLoadAider返回出去，
 *      没有成功则将失败原因返回
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface OnLoadListener {

    /**
     * 文件数据加载成功
     *
     * @param result
     */
    void onLoadOK(Obj3DLoadResult result);

    /**
     * 文件数据加载失败
     *
     * @param failedMsg
     */
    void onLoadFailed(String failedMsg);
}
