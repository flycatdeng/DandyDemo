package com.dandy.module.ms3dload;

import com.dandy.helper.java.math.Vec3;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DTriangle implements Serializable {
    private int flag;//标志，该三角形在编辑器中的状态
    private int[] indexs;         //组装索引值
    private Vec3[] normals;     //3个顶点法线向量
    private Vec3 s;             //三个顶点的纹理S坐标
    private Vec3 t;             //三个顶点的纹理T坐标
    private byte smoothingGroup; //平滑组
    private byte groupIndex;     //组索引

    public MS3DTriangle(int flag, int[] indexs, Vec3[] normals, Vec3 s, Vec3 t, byte smoothingGroup, byte groupIndex) {
        this.flag = flag;
        this.indexs = indexs;
        this.normals = normals;
        this.s = s;
        this.t = t;
        this.smoothingGroup = smoothingGroup;
        this.groupIndex = groupIndex;
    }

    public int getFlag() {
        return flag;
    }

    public int[] getIndexs() {
        return indexs;
    }

    public Vec3[] getNormals() {
        return normals;
    }

    public Vec3 getS() {
        return s;
    }

    public Vec3 getT() {
        return t;
    }

    public byte getSmoothingGroup() {
        return smoothingGroup;
    }

    public byte getGroupIndex() {
        return groupIndex;
    }

    @Override
    public String toString() {
        return "MS3DTriangle{" +
                "indexs=" + Arrays.toString(indexs) +
                ", normals=" + Arrays.toString(normals) +
                ", s=" + s +
                ", t=" + t +
                ", smoothingGroup=" + smoothingGroup +
                ", groupIndex=" + groupIndex +
                '}';
    }
}
