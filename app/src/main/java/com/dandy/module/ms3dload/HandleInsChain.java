package com.dandy.module.ms3dload;

import com.dandy.helper.java.io.SmallEndianInputStream;

import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleInsChain implements IHandleInsChain {
    private IHandleInsChain mNextHandler;

    @Override
    public void setNextHandler(IHandleInsChain handler) {
        mNextHandler = handler;
    }

    @Override
    public void handleIns(SmallEndianInputStream fis, MS3DData data, IOnLoadCallback callback) {
        try {
            onOwnLogicHandle(data, fis);
        } catch (Exception e) {//如果有异常则捕获异常，并停止往下处理，回调告知处理失败了
            callback.onLoadFaild(e.getMessage());
            return;
        }
        if (mNextHandler == null) {//无需再向下处理了
            data.stopCountParseTime();
            callback.onLoadOK(data);
        } else {
            mNextHandler.handleIns(fis, data, callback);
        }
    }

    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
    }
}
