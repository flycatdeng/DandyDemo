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
}
