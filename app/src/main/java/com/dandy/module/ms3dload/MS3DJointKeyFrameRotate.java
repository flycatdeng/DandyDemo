package com.dandy.module.ms3dload;

import com.dandy.helper.java.math.Vec3;
import com.dandy.helper.java.math.Vec4;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DJointKeyFrameRotate implements Serializable {
    private float time;//读取关键帧时间,单位为秒
    private Vec3 eulerAngle;//关键寻转的欧拉角
    private Vec4 quaternionFromEulerAngle;//

    public MS3DJointKeyFrameRotate(float time, Vec3 eulerAngle) {
        this.time = time;
        this.eulerAngle = eulerAngle;
        eulerAngleToQuaternion();
    }

    private void eulerAngleToQuaternion() {
        //TODO
    }
}
