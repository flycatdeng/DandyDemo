package com.dandy.helper.java.math;

import com.dandy.helper.java.basedata.FloatHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     三维向量
 * </pre>
 * Created by flycatdeng on 2017/4/12.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vec3 implements Serializable, Vec<Vec3> {
    public float x;
    public float y;
    public float z;

    public Vec3() {
    }

    public Vec3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3(float sameValue) {
        this(sameValue, sameValue, sameValue);
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 叉乘一个向量,得到一个新的向量
     *
     * @param vec
     * @return
     */
    public Vec3 crossProduct(Vec3 vec) {
        Vec3 result = new Vec3();
        result.x = y * vec.z - z * vec.y;
        result.y = z * vec.x - x * vec.z;
        result.z = x * vec.y - y * vec.x;
        return result;
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public Vec3 copy() {
        return new Vec3(this);
    }

    @Override
    public Vec3 add(Vec3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    @Override
    public Vec3 add(float value) {
        this.x += value;
        this.y += value;
        this.z += value;
        return this;
    }

    @Override
    public Vec3 add(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return add((float) vec.get(0));
        } else if (len == 3) {
            this.x += (float) vec.get(0);
            this.y += (float) vec.get(1);
            this.z += (float) vec.get(2);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 3 while yours is " + len);
        }
    }

    @Override
    public Vec3 minus(Vec3 vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    @Override
    public Vec3 minus(float value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }

    @Override
    public Vec3 minus(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return minus((float) vec.get(0));
        } else if (len == 3) {
            this.x -= (float) vec.get(0);
            this.y -= (float) vec.get(1);
            this.z -= (float) vec.get(2);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 3 while yours is " + len);
        }
    }


    @Override
    public Vec3 normalize() {
        return floatArrayToVec(FloatHelper.normalize(this.toFloatArray()));
    }

    @Override
    public float[] toFloatArray() {
        return new float[]{x, y, z};
    }

    @Override
    public float getItemSum() {
        return FloatHelper.sum(this.toFloatArray());
    }

    /***********************************************************************/
    public static float[] vecToFloatArray(Vec3 vec3) {
        return new float[]{vec3.x, vec3.y, vec3.z};
    }

    public static Vec3 floatArrayToVec(float[] floats) {
        if (floats == null) {
            throw new RuntimeException("floats array list can not be null");
        }
        if (floats.length == 1) {
            return new Vec3(floats[0]);
        } else if (floats.length == 3) {
            return new Vec3(floats[0], floats[1], floats[2]);
        } else {
            throw new RuntimeException("floats array list length must be 1 or 3");
        }
    }
}
