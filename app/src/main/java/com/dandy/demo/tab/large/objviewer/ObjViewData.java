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

}
