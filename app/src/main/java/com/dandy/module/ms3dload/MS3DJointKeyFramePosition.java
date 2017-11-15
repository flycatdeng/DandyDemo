package com.dandy.module.ms3dload;

import com.dandy.helper.java.math.Vec3;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DJointKeyFramePosition implements Serializable {
    private float time;
    private Vec3 translate;

    public MS3DJointKeyFramePosition(float time, Vec3 translate) {
        this.time = time;
        this.translate = translate;
    }
}
