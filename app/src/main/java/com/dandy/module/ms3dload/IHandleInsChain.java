package com.dandy.module.ms3dload;

import com.dandy.helper.java.io.SmallEndianInputStream;

/**
 * <pre>
 *     处理ms3d文件流的职责链设计模式接口
 *     调用{@link #setNextHandler(IHandleInsChain)}} 设置职责链接下来被谁处理，如果是传进来的是null则不再处理
 *     调用{@link #handleIns(SmallEndianInputStream, MS3DData, IOnLoadCallback)}来处理具体的流逻辑
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface IHandleInsChain {
    void setNextHandler(IHandleInsChain handler);

    void handleIns(SmallEndianInputStream fis, MS3DData data, IOnLoadCallback callback);
}
