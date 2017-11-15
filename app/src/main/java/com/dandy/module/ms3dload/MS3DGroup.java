package com.dandy.module.ms3dload;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *     组信息
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DGroup implements Serializable {
    private byte flag;
    private String name;//名称
    private int[] indicies;        //组内的三角形的对应索引
    private byte materialIndex;    //材质索引

    public MS3DGroup(byte flag, String name, int[] indicies, byte materialIndex) {
        this.indicies = indicies;
        this.materialIndex = materialIndex;
        this.flag = flag;
        this.name = name;
    }

    public byte getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public int[] getIndicies() {
        return indicies;
    }

    public byte getMaterialIndex() {
        return materialIndex;
    }

    @Override
    public String toString() {
        return "MS3DGroup{" +
                "indicies=" + Arrays.toString(indicies) +
                ", materialIndex=" + materialIndex +
                '}';
    }
}
