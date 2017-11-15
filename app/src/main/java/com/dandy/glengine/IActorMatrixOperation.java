package com.dandy.glengine;

import com.dandy.helper.java.math.Vec3;

/**
 * <pre>
 *     作为一个Actor的矩阵操作方面的一些基本的方法
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface IActorMatrixOperation {
    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    void translate(float x, float y, float z);

    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    void translate(Vec3 offset);

    /**
     * 设置最终移动量
     */
    void setTranslation(float x, float y, float z);

    /**
     * <pre>
     * 设置绕xyz轴转动
     * </pre>
     */
    void rotate(float angle, float x, float y, float z);

    /**
     * 缩放
     *
     * @param x
     * @param y
     * @param z
     */
    void scale(float x, float y, float z);

    /**
     * 缩放
     */
    void scale(Vec3 scale);

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
    void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz);

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
    void setProjectFrustum(float left, float right, float bottom, float top, float near, float far);

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
    void setProjectOrtho(float left, float right, float bottom, float top, float near, float far);

    /**
     * 得到总变换矩阵
     *
     * @return
     */
    float[] getMVPMatrix();

    /**
     * 得到模型变换矩阵
     *
     * @return
     */
    float[] getModelMatrix();

    /**
     * 得到视图矩阵
     *
     * @return
     */
    float[] getViewMatrix();

    /**
     * 得到模型视图变换矩阵
     *
     * @return
     */
    float[] getModelViewMatrix();

    /**
     * 得到投影矩阵
     *
     * @return
     */
    float[] getProjectMatrix();
}
