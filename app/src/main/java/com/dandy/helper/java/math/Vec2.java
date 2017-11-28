package com.dandy.helper.java.math;

import com.dandy.helper.java.basedata.FloatHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     二维向量
 * </pre>
 * Created by flycatdeng on 2017/4/14.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vec2 implements Serializable, Vec<Vec2> {
    public float x;
    public float y;

    public Vec2() {
    }

    public Vec2(Vec2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2(float sameValue) {
        this(sameValue, sameValue);
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vec2 copy() {
        return new Vec2(this);
    }

    @Override
    public Vec2 add(Vec2 vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    @Override
    public Vec2 add(float value) {
        this.x += value;
        this.y += value;
        return this;
    }

    @Override
    public Vec2 add(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return add((float) vec.get(0));
        } else if (len == 2) {
            this.x += (float) vec.get(0);
            this.y += (float) vec.get(1);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 2 while yours is " + len);
        }
    }

    @Override
    public Vec2 minus(Vec2 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    @Override
    public Vec2 minus(float value) {
        this.x -= value;
        this.y -= value;
        return this;
    }

    @Override
    public Vec2 minus(float... values) {
        List vec = Arrays.asList(values);
        int len = vec.size();
        if (len == 1) {
            return minus((float) vec.get(0));
        } else if (len == 2) {
            this.x -= (float) vec.get(0);
            this.y -= (float) vec.get(1);
            return this;
        } else {
            throw new RuntimeException("the length should be 1 or 2 while yours is " + len);
        }
    }

    @Override
    public Vec2 multiply(float sameValue) {
        this.x *= sameValue;
        this.y *= sameValue;
        return this;
    }

    @Override
    public Vec2 multiply(Vec2 vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    @Override
    public Vec2 normalize() {
        return floatArrayToVec(FloatHelper.normalize(this.toFloatArray()));
    }

    @Override
    public float[] toFloatArray() {
        return new float[]{x, y};
    }

    @Override
    public float getItemSum() {
        return FloatHelper.sum(this.toFloatArray());
    }

    /**********************************************************************************************/
    public static float[] vecToFloatArray(Vec2 vec) {
        return new float[]{vec.x, vec.y};
    }

    public static Vec2 floatArrayToVec(float[] floats) {
        if (floats == null) {
            throw new RuntimeException("floats array list can not be null");
        }
        if (floats.length == 1) {
            return new Vec2(floats[0]);
        } else if (floats.length == 2) {
            return new Vec2(floats[0], floats[1]);
        } else {
            throw new RuntimeException("floats array list length must be 1 or 2");
        }
    }
}
