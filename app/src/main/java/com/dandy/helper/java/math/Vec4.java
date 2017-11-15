package com.dandy.helper.java.math;

import com.dandy.helper.java.basedata.FloatHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     四维向量
 * </pre>
 * Created by flycatdeng on 2017/4/12.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vec4 implements Serializable, Vec<Vec4> {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4() {
    }

    public Vec4(float sameValue) {
        this(sameValue, sameValue, sameValue, sameValue);
    }

    public Vec4(Vec4 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.w = vec.w;
    }

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public String toString() {
        return "Vec4{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }

    @Override
    public Vec4 copy() {
        return new Vec4(this);
    }

    @Override
    public Vec4 add(Vec4 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        this.w += vec.w;
        return this;
    }

    @Override
    public Vec4 add(float value) {
        this.x += value;
        this.y += value;
        this.z += value;
        this.w += value;
        return this;
    }

    @Override
    public Vec4 add(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return add((float) vec.get(0));
        } else if (len == 4) {
            this.x += (float) vec.get(0);
            this.y += (float) vec.get(1);
            this.z += (float) vec.get(2);
            this.w += (float) vec.get(3);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 4 while yours is " + len);
        }
    }

    @Override
    public Vec4 minus(Vec4 vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        w -= vec.w;
        return this;
    }

    @Override
    public Vec4 minus(float value) {
        x -= value;
        y -= value;
        z -= value;
        w -= value;
        return this;
    }

    @Override
    public Vec4 minus(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return minus((float) vec.get(0));
        } else if (len == 4) {
            this.x -= (float) vec.get(0);
            this.y -= (float) vec.get(1);
            this.z -= (float) vec.get(2);
            this.w -= (float) vec.get(3);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 4 while yours is " + len);
        }
    }

    @Override
    public Vec4 normalize() {
        return floatArrayToVec(FloatHelper.normalize(this.toFloatArray()));
    }

    @Override
    public float[] toFloatArray() {
        return new float[]{x, y, z, w};
    }

    @Override
    public float getItemSum() {
        return FloatHelper.sum(this.toFloatArray());
    }

    /**********************************************************************************************/
    public static float[] vecToFloatArray(Vec4 vec) {
        return new float[]{vec.x, vec.y, vec.z, vec.w};
    }

    public static Vec4 floatArrayToVec(float[] floats) {
        if (floats == null) {
            throw new RuntimeException("floats array list can not be null");
        }
        if (floats.length == 1) {
            return new Vec4(floats[0]);
        } else if (floats.length == 4) {
            return new Vec4(floats[0], floats[1], floats[2], floats[3]);
        } else {
            throw new RuntimeException("floats array list length must be 1 or 4");
        }
    }
}
