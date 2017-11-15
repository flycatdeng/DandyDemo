package com.dandy.module.obj3dload;

import com.dandy.helper.android.LogHelper;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *      解析obj文件之后得到的结果，包括
 *          顶点坐标个数，及对应的每个坐标的xyz值
 *          法向量个数及其对应每个向量的xyz值
 *          纹理坐标个数及其每个坐标对应的st值
 *          面（三角形）的个数
 *          加载的时间
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class Obj3DLoadResult implements Serializable {
    private long parseTime = 0L;//解析总共使用的时间
    private int numVerts = 0;//顶点坐标个数
    private int numTex = 0;//纹理坐标个数
    private int numNorms = 0;//法向量个数
    private int numFaces = 0;//面个数
    private float[] vertexXYZ;//顶点坐标数组
    private float[] normalVectorXYZ;//法向量数组
    private float[] textureVertexST;//纹理坐标数组

    public Obj3DLoadResult(long parseTime, int numVerts, int numTex, int numNorms, int numFaces, float[] vertexXYZ, float[] normalVectorXYZ, float[] textureVertexST) {
        this.parseTime = parseTime;
        this.numVerts = numVerts;
        this.numTex = numTex;
        this.numNorms = numNorms;
        this.numFaces = numFaces;
        this.vertexXYZ = vertexXYZ;
        this.normalVectorXYZ = normalVectorXYZ;
        this.textureVertexST = textureVertexST;
    }

    /**
     * 顶点坐标每个坐标的xyz值
     *
     * @return
     */
    public float[] getVertexXYZ() {
        return vertexXYZ;
    }

    /**
     * 法向量每个坐标的xyz值
     *
     * @return
     */
    public float[] getNormalVectorXYZ() {
        return normalVectorXYZ;
    }

    /**
     * 纹理坐标每个坐标的st值
     *
     * @return
     */
    public float[] getTextureVertexST() {
        return textureVertexST;
    }

    /**
     * 顶点坐标个数
     *
     * @return
     */
    public int getVextexCount() {
        return numVerts;
    }

    /**
     * 法向量个数
     *
     * @return
     */
    public int getNormalVectorCount() {
        return numNorms;
    }

    /**
     * 纹理坐标个数
     *
     * @return
     */
    public int getTextureCoorCount() {
        return numTex;
    }

    /**
     * 面（三角形）的个数
     *
     * @return
     */
    public int getTrianglesCount() {
        return numFaces;
    }

    /**
     * 加载的时间,以ms记
     *
     * @return
     */
    public long getLoadTime() {
        return parseTime;
    }

    @Override
    public String toString() {
        return "Obj3DLoadResult{" +
                "parseTime=" + parseTime +
                ", numVerts=" + numVerts +
                ", numTex=" + numTex +
                ", numNorms=" + numNorms +
                ", numFaces=" + numFaces +
                ", vertexXYZ.length =" +vertexXYZ.length +
                ", normalVectorXYZ.length=" + normalVectorXYZ.length +
                ", textureVertexST.length=" + (textureVertexST==null?"null":(textureVertexST.length+"")) +
                '}';
    }
}
