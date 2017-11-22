package com.dandy.helper.opengl;

import android.opengl.Matrix;

import java.util.Arrays;

/**
 * <pre>
 *      OpenGL_ES中矩阵变换，包括
 *          物体本身变换：旋转、平移、缩放
 *          观察点变换：相机位置
 *          投影变换：正交投影、透视投影
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */
public class MVPMatrixAider {
    private static final String TAG = "MVPMatrixAider";
    private float[] mProjectMatrix = new float[16];// 4x4矩阵 投影用
    private float[] mViewMatrix = new float[16];// 摄像机位置朝向9参数矩阵
    private float[] mModelMatrix = new float[16];// 具体物体的移动旋转矩阵，旋转、平移、缩放
    private float[] mMVMatrix = new float[16];// 模型和摄像机位置的变换矩阵
    private float[] mMVPMatrix = new float[16];// 最后起作用的总变换矩阵
    private float[] mSetMVPMatrix;//用于设置的总变换矩阵
    private float[] mViewProjMatrix = new float[16];
    private ProjectType mProjectType;//投影类型，正交、透视

    public void onDestroy() {
        mProjectMatrix = null;
        mViewMatrix = null;
        mModelMatrix = null;
        mMVMatrix = null;
        mMVPMatrix = null;
        mViewProjMatrix = null;
    }

    public float[] getViewProjMatrix() {
        Matrix.multiplyMM(mViewProjMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        return mViewProjMatrix;
    }

    /**
     * <pre>
     *     得到物体本身变换以及观察点变换以及投影矩阵综合的总变换矩阵
     *     如果设置的MVP矩阵不为null的话则会有限使用从外部设置的MVP矩阵
     * </pre>
     *
     * @return
     */
    public float[] getMVPMatrix() {
        if (mSetMVPMatrix != null) {
            return mSetMVPMatrix;
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);//左乘，mViewMatrix左乘mModelMatrix的结果给mMVPMatrix，其实就是mMVMatrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);//mMVMatrix左乘mProjectMatrix 得到mMVPMatrix
        return mMVPMatrix;
    }

    /**
     * 可以手动设置MVP矩阵，因为不一定是由自己计算出来的，例如相机等就可以直接得到一个最终的MVP矩阵
     *
     * @param matrix
     */
    public void setMVPMatrix(float[] matrix) {
        if (matrix == null) {
            mSetMVPMatrix = null;
            return;
        }
        if (matrix.length != 16) {
            throw new RuntimeException("setMVPMatrix matrix.length must be 16");
        }
        mSetMVPMatrix = Arrays.copyOf(matrix, matrix.length);
    }

    /**
     * 得到物体本身变换和观察点变化的综合矩阵
     *
     * @return
     */
    public float[] getModelViewMatrix() {
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);//左乘，mViewMatrix左乘mModelMatrix得到mMVMatrix
        return mMVMatrix;
    }

    /**
     * 得到投影矩阵
     *
     * @return
     */
    public float[] getProjectMatrix() {
        return mProjectMatrix;
    }

    /**
     * 得到相机位置矩阵
     *
     * @return
     */
    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    /**
     * 得到变换后的物体的矩阵
     *
     * @return
     */
    public float[] getModelMatrix() {
        return mModelMatrix;
    }

    /**
     * 投影类型
     *
     * @return
     */
    public ProjectType getProjectType() {
        return mProjectType;
    }

    /**
     * <pre>
     * 获取不变换初始矩阵
     * </pre>
     */
    public void setInitStack() {
        Matrix.setRotateM(mModelMatrix, 0, 0, 1, 0, 0);
    }

    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(mModelMatrix, 0, x, y, z);
    }

    /**
     * <pre>
     * 设置绕xyz轴转动
     * </pre>
     */
    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mModelMatrix, 0, angle, x, y, z);
    }

    /**
     * 缩放
     *
     * @param x
     * @param y
     * @param z
     */
    public void scale(float x, float y, float z)//设置沿xyz轴缩放
    {
        Matrix.scaleM(mModelMatrix, 0, x, y, z);
    }

    /**
     * 设置摄像机
     *
     * @param cx  摄像机位置x
     * @param cy  摄像机位置y
     * @param cz  摄像机位置z
     * @param tx  摄像机目标点x
     * @param ty  摄像机目标点y
     * @param tz  摄像机目标点z
     * @param upx 摄像机UP向量X分量
     * @param upy 摄像机UP向量Y分量
     * @param upz 摄像机UP向量Z分量
     */
    public void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        Matrix.setLookAtM(mViewMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    /**
     * 设置透视投影参数
     *
     * @param left   near面的left
     * @param right  near面的right
     * @param bottom near面的bottom
     * @param top    near面的top
     * @param near   near面距离to view point
     * @param far    far面距离to view point
     */
    public void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjectMatrix, 0, left, right, bottom, top, near, far);
        mProjectType = ProjectType.FRUSTUM;
    }

    /**
     * 设置正交投影参数
     *
     * @param left   near面的left
     * @param right  near面的right
     * @param bottom near面的bottom
     * @param top    near面的top
     * @param near   near面距离
     * @param far    far面距离
     */
    public void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjectMatrix, 0, left, right, bottom, top, near, far);
        mProjectType = ProjectType.ORTHO;
    }

    /**
     * 投影类型
     */
    enum ProjectType {
        ORTHO, FRUSTUM
    }
}
