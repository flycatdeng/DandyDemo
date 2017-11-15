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

public class HandleJointKeyFrameRotateIns {

    /**
     * 加载关节旋转的关键帧信息
     *
     * @param is
     * @param num
     */
    public static MS3DJointKeyFrameRotate[] load(SmallEndianInputStream is, int num) throws IOException {
        MS3DJointKeyFrameRotate[] rotates = new MS3DJointKeyFrameRotate[num];
        for (int i = 0; i < num; i++) {//循环加载所有的旋转关键帧信息
            float time = is.readFloat();//读取关键帧时间,单位为秒
            Vec3 eulerAngle = new Vec3(is.readFloat(), is.readFloat(), is.readFloat());
            rotates[i] = new MS3DJointKeyFrameRotate(time, eulerAngle);
        }
        return rotates;
    }
}
