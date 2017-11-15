package com.dandy.module.ms3dload;

import com.dandy.helper.java.io.SmallEndianInputStream;
import com.dandy.helper.java.math.Vec3;

import java.io.IOException;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleJointKeyFramePositionIns {
    /**
     * 加载关节平移的关键帧信息
     *
     * @param is
     * @param num
     */
    public static MS3DJointKeyFramePosition[] load(SmallEndianInputStream is, int num) throws IOException {
        MS3DJointKeyFramePosition[] positions = new MS3DJointKeyFramePosition[num];
        for (int i = 0; i < num; i++) {//循环加载所有的旋转关键帧信息
            float time = is.readFloat();//读取关键帧时间,单位为秒
            Vec3 translate = new Vec3(is.readFloat(), is.readFloat(), is.readFloat());//读取关键帧位置信息
            positions[i] = new MS3DJointKeyFramePosition(time, translate);
        }
        return positions;
    }
}
