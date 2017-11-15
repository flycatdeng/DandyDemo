package com.dandy.glengine;

/**
 * <pre>
 *     作为一个Actor的渲染线程方面的一些基本的方法
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface IGLActor {
    /**
     * surface被创建
     */
    void onSurfaceCreated();

    /**
     * surface更改了信息（主要是宽高变化了），需要重新设置投影矩阵和相机位置
     *
     * @param width
     * @param height
     */
    void onSurfaceChanged(int width, int height);

    /**
     * 渲染一帧
     */
    void onDrawFrame();

    /**
     * surface是否已经被创建好了（其实就是IGLActor的{@link #isSurfaceCreated()}是否已经被调用了）
     *
     * @return
     */
    boolean isSurfaceCreated();

    /**
     * 请求渲染
     */
    void requestRender();

    /**
     * 设置渲染回调
     *
     * @param listener
     */
    void setRequestRenderListener(RequestRenderListener listener);

    RequestRenderListener getRequestRenderListener();

    /**
     * 渲染监听回调
     */
    interface RequestRenderListener {
        public void onRequestRenderCalled();
    }
}
