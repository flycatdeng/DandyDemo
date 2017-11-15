package com.dandy.module.ms3dload;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DFrameTime implements Serializable {
    private float fps;
    private float currentTime;
    private float frameCount;
    private float totalTime;

    public MS3DFrameTime(float fps, float currentTime, float frameCount, float totalTime) {
        this.fps = fps;
        this.currentTime = currentTime;
        this.frameCount = frameCount;
        this.totalTime = totalTime;
    }

    public float getFps() {
        return fps;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getFrameCount() {
        return frameCount;
    }

    public float getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "MS3DFrameTime{" +
                "fps=" + fps +
                ", currentTime=" + currentTime +
                ", frameCount=" + frameCount +
                ", totalTime=" + totalTime +
                '}';
    }
}
