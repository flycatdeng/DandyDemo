package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
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

public class HandleFrameTimeIns extends HandleInsChain {
    private static final String TAG = "HandleFrameTimeIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        float fps = fis.readFloat();//加载帧速率信息
        float current_time = fis.readFloat();//当前时间
        float frame_count = fis.readInt();//关键帧数
        float totalTime = frame_count / fps;//计算动画总时间
        MS3DFrameTime frameTime = new MS3DFrameTime(fps, current_time, frame_count, totalTime);
        data.setFrameTime(frameTime);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + frameTime.toString());
        }
    }
}
