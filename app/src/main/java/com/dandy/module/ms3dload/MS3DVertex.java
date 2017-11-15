package com.dandy.module.ms3dload;

import com.dandy.helper.java.math.Vec3;

import java.io.Serializable;

/**
 * <pre>
 *     顶点信息类
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DVertex implements Serializable {
    private byte flagFirst = -1;//该顶点在编辑中的状态
    private Vec3 originPosition;    //从文件中读取的顶点原始xyz坐标
    private Vec3 realTimePosition;    //动画中实时变化的顶点xyz坐标
    private byte boneID;            //骨骼ID =-1则表明此顶点没有绑定任何骨骼
    private byte flagLast = -1;//目前不包含有用信息

    public MS3DVertex(byte flagFirst,Vec3 originPosition, byte boneID,byte flagLast) {
        this.flagFirst = flagFirst;
        this.originPosition = originPosition;
        this.boneID = boneID;
        this.flagLast = flagLast;
    }

    public byte getFlagFirst() {
        return flagFirst;
    }

    public byte getFlagLast() {
        return flagLast;
    }

    public Vec3 getOriginPosition() {
        return originPosition;
    }

    public Vec3 getRealTimePosition() {
        return realTimePosition;
    }

    public byte getBoneID() {
        return boneID;
    }

    public void setRealTimePosition(Vec3 realTimePosition) {
        this.realTimePosition = realTimePosition;
    }

    @Override
    public String toString() {
        return "MS3DVertex{" +
                "originPosition=" + originPosition +
                ", realTimePosition=" + realTimePosition +
                ", boneID=" + boneID +
                '}';
    }
}
