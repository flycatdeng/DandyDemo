package com.dandy.helper.opengl;

import android.opengl.Matrix;

import com.dandy.helper.java.math.Vec3;

import java.io.Serializable;

/**
 * <pre>
 *     用于计算的4x4矩阵
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class Matrix4X4 implements Serializable {
    //用于存储矩阵元素的数组
    private float[] matrix = new float[16];

    /**
     * 将矩阵设置为单位矩阵
     */
    public final void loadIdentity() {
        Matrix.setIdentityM(matrix, 0);
    }

    /**
     * 设置旋转矩阵参数，入口参数为欧拉角三个分量组成的三维向量
     * 实质上是将欧拉角形式的旋转描述转化为矩阵形式的旋转描述
     *
     * @param angles
     */
    public void genRotationFromEulerAngle(Vec3 angles) {
        float yaw = angles.x;
        float pitch = angles.y;
        float roll = angles.z;

        final double cr = Math.cos(yaw);
        final double sr = Math.sin(yaw);
        final double cp = Math.cos(pitch);
        final double sp = Math.sin(pitch);
        final double cy = Math.cos(roll);
        final double sy = Math.sin(roll);
        final double srsp = sr * sp;
        final double crsp = cr * sp;

        float[] mTemp = new float[16];
        Matrix.setIdentityM(mTemp, 0);

        mTemp[0] = (float) (cp * cy);
        mTemp[1] = (float) (cp * sy);
        mTemp[2] = (float) (-sp);

        mTemp[4] = (float) (srsp * cy - cr * sy);
        mTemp[5] = (float) (srsp * sy + cr * cy);
        mTemp[6] = (float) (sr * cp);

        mTemp[8] = (float) (crsp * cy + sr * sy);
        mTemp[9] = (float) (crsp * sy - sr * cy);
        mTemp[10] = (float) (cr * cp);

        float[] mTempR = new float[16];
        Matrix.multiplyMM(mTempR, 0, this.matrix, 0, mTemp, 0);
        this.matrix = mTempR;
    }

    /**
     * 设置平移矩阵参数
     *
     * @param translation
     */
    public void setTranslation(Vec3 translation) {
        this.matrix[12] = translation.x;
        this.matrix[13] = translation.y;
        this.matrix[14] = translation.z;
    }

    /**
     * 两个矩阵相乘
     *
     * @param m1
     * @param m2
     * @return
     */
    public final Matrix4X4 mul(Matrix4X4 m1, Matrix4X4 m2) {
        float[] mData = new float[16];
        Matrix.multiplyMM(mData, 0, m1.matrix, 0, m2.matrix, 0);
        this.matrix = mData;
        return this;
    }

    /**
     * 把一个矩阵的元素值依次拷贝到本矩阵中
     *
     * @param m
     */
    public final void copyFrom(Matrix4X4 m) {
        for (int i = 0; i < 16; i++) {
            this.matrix[i] = m.matrix[i];
        }
    }
}
