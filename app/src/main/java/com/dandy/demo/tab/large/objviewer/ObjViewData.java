package com.dandy.demo.tab.large.objviewer;

import android.opengl.GLES20;

/**
 * <pre>
 *
 * </pre>
 * Created by QueenJar
 * Wechat: queenjar
 * Emial: queenjar@qq.com
 */

public class ObjViewData {
    private static ObjViewData mObjViewData = new ObjViewData();

    private ObjViewData() {
    }

    public synchronized static ObjViewData getInstance() {
        return mObjViewData;
    }

    /**
     * <pre>
     *     由于用于绘制的顶点数据都是三个顶点一组的
     *     默认绘制三角面 GLES20.GL_TRIANGLES
     *     还能绘制三角形 GLES20.GL_LINE_STRIP
     * </pre>
     */
    public int primitiveMode = GLES20.GL_TRIANGLES;//

    /**
     * <pre>
     *     投影模式
     *     {@link #PERSPECTIVE} 透视投影
     *     @link #ORTHO} 正交投影
     * </pre>
     */
    public enum ProjectMode {
        PERSPECTIVE, ORTHO
    }

    /**
     * 投影模式，默认为透视投影
     */
    public ProjectMode projectMode = ProjectMode.PERSPECTIVE;
    /**
     * <p style="color:red">near面</p>的left
     */
    public float projectLeft = -0.5f;
    /**
     * <p style="color:red">near面</p>的Right
     */
    public float projectRight = -0.5f;
    /**
     * <p style="color:red">near面</p>的Bottom
     */
    public float projectBottom = -0.5f;
    /**
     * <p style="color:red">near面</p>的Top
     */
    public float projectTop = -0.5f;
    /**
     * near面距离<p style="color:red">视点的</p>距离
     */
    public float projectNear = 2;
    /**
     * far面距离<p style="color:red">视点的</p>距离
     */
    public float projectFar = 1111;

    /**
     * 模型在xyz轴方向上的缩放
     */
    public float scale = 0.2f;
    public Light light = new Light();

    enum LightMode {
        POINT, DIRECT
    }

    class Light {
        public LightMode lightMode = LightMode.DIRECT;
        public float lightPosX = 20f;//0~100
        public float lightPosY = 20f;//0~100
        public float lightPosZ = 20f;//0~100
        public float ambentIntensity = 0.15f;//0.0~1.0
        public float diffuseIntensity = 0.8f;//0.0~1.0
        public float specularIntensity = 1.0f;//0.0~1.0
        public float specularShininess = 10.0f;//0~100

        @Override
        public String toString() {
            return "Light{" +
                    "lightMode=" + lightMode +
                    ", lightPosX=" + lightPosX +
                    ", lightPosY=" + lightPosY +
                    ", lightPosZ=" + lightPosZ +
                    ", ambentIntensity=" + ambentIntensity +
                    ", diffuseIntensity=" + diffuseIntensity +
                    ", specularIntensity=" + specularIntensity +
                    ", specularShininess=" + specularShininess +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ObjViewData{" +
                "primitiveMode=" + primitiveMode +
                ", projectMode=" + projectMode +
                ", projectLeft=" + projectLeft +
                ", projectRight=" + projectRight +
                ", projectBottom=" + projectBottom +
                ", projectTop=" + projectTop +
                ", projectNear=" + projectNear +
                ", projectFar=" + projectFar +
                ", scale=" + scale +
                ", light=" + light.toString() +
                '}';
    }
}
