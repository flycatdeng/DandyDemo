package com.dandy.module.ms3dload;

/**
 * <pre>
 *
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface IOnLoadCallback {
    void onLoadOK(MS3DData data);

    void onLoadFaild(String msg);
}
