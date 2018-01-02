package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.glengine.Actor;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.math.Vec3;
import com.dandy.helper.java.nio.ArrayToBufferHelper;
import com.dandy.helper.opengl.TextureOptions;
import com.dandy.helper.opengl.VBOHelper;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      3D模型的父类
 *      基本的shader属性为(扩展的需要自己添加，但是别更改以下信息)：
 *          1.顶点位置信息vec3 aPosition
 *          2.法线向量vec2 aNormal
 *          3.顶点纹理坐标vec2 aTexCoor
 *          4.总变换矩阵mat4 uMVPMatrix
 *          5.变换矩阵mat4 uMMatrix
 *          6.光源位置信息vec3 uLightLocation
 *          7.摄像机位置vec3 uCamera
 * </pre>
 * Created by flycatdeng on 2017/4/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */
public class ActorObjView3D extends Actor {
    private static final String TAG = "ActorObjView3D";
    private ObjViewData mObjViewData = new ObjViewData();
    private FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
    private FloatBuffer mNormalBuffer;// 顶点法向量数据缓冲
    private FloatBuffer mTexCoorBuffer;// 顶点纹理坐标数据缓冲
    private FloatBuffer mLightLocationBuffer;// 顶点纹理坐标数据缓冲
    private FloatBuffer mCameraLocationBuffer;// 顶点纹理坐标数据缓冲
    private int muMVPMatrixHandle = -1;// 总变换矩阵引用
    private int muModelMatrixHandle = -1;// 位置、旋转变换矩阵
    private int muViewMatrixHandle = -1;
    private int muModelViewMatrixHandle = -1;
    private int maPositionHandle = -1; // 顶点位置属性引用
    private int maNormalHandle = -1;// 顶点法向量属性引用
    private int maTexCoorHandle = -1; // 顶点纹理坐标属性引用
    private int muTextureHandle = -1;//纹理引用
    private int muHasTexCoorHandle = -1;//是否有纹理坐标句柄
    private int muHasTextureHandle = -1;//是否有纹理图句柄
    private int muLightLocationHandle = -1;//光源位置句柄
    private int muCameraLocationHandle = -1;//摄像机位置句柄
    private boolean isDataOK = false;
    //    private int vboId[] = new int[3];
    private float mMatHasTexCoor = 1f;
    private float mMatHasTexture = 0f;
    private List<VBOHelper.VBOOptions> mVBOOptions = new ArrayList<>();

    public ActorObjView3D(Context context) {
        super(context);
        mDefaultTextureOptions = TextureOptions.defaultMipmapOptions();
        mDefaultMaterialName = "demo/objview/objview3d.mat";
    }

    public void loadFromData(final float[] vertices, final float[] normals, final float texCoors[]) {
        init(vertices, normals, texCoors);
    }

    private void init(float[] vertices, float[] normals, float[] texCoors) {
        // 初始化顶点坐标与着色数据
        initVertexData(vertices, normals, texCoors);
        isDataOK = true;
        requestRender();
    }

    // 初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        if (texCoors == null) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " texCoors == null");
            mMatHasTexCoor = 0f;
        }
        LogHelper.d(TAG, LogHelper.getThreadName() + "count vertices=" + vertices.length + " normals=" + normals.length + " texCoors=" + (mMatHasTexCoor == 0f ? "null" : texCoors.length + ""));
        mVertexCount = vertices.length / 3;
        mVertexBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertices);
        mNormalBuffer = ArrayToBufferHelper.floatArrayToBuffer(normals);
        if (mMatHasTexCoor == 1f) {
            mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(texCoors);
        }
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        addRunOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                int target = GLES20.GL_ARRAY_BUFFER;
                int usage = GLES20.GL_STATIC_DRAW;
                VBOHelper.VBOOptions vertexVBO = VBOHelper.VBOOptions.obtainDrawOptions("vertexVBO", target, mVertexBuffer.capacity() * 4, mVertexBuffer, usage,
                        maPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
                mVBOOptions.add(vertexVBO);
                VBOHelper.VBOOptions normalVBO = VBOHelper.VBOOptions.obtainDrawOptions("normalVBO", target, mNormalBuffer.capacity() * 4, mNormalBuffer, usage,
                        maNormalHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
                mVBOOptions.add(normalVBO);
                if (mMatHasTexCoor == 1f) {
                    VBOHelper.VBOOptions TexCoorVBO = VBOHelper.VBOOptions.obtainDrawOptions("TexCoorVBO", target, mTexCoorBuffer.capacity() * 4, mTexCoorBuffer, usage,
                            maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
                    mVBOOptions.add(TexCoorVBO);
                }
                VBOHelper.initVBOs(mVBOOptions, true);
            }
        });
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        // 调用此方法计算产生透视投影矩阵
        setProjectFrustum(-ratio, ratio, -1, 1, 2, 1111);
        // 调用此方法产生摄像机9参数位置矩阵
        setCamera(0f, 0f, 50.0f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 0.0f);
        setCameraLocation(new Vec3(0f, 0f, 50.0f));
        setLightLocation(new Vec3(20f));
    }

    @Override
    protected void onShaderLocationInit() {
        muMVPMatrixHandle = getMVPMatrixHandle();
        muModelMatrixHandle = getModelMatrixHandle();
        muViewMatrixHandle = getViewMatrixHandle();
        muModelViewMatrixHandle = getModelViewMatrixHandle();
        maPositionHandle = getPositionHandle();
        maNormalHandle = getNormalHandle();
        maTexCoorHandle = getTexCoorHandle();
        muTextureHandle = getTextureHandle();
        muHasTexCoorHandle = getHasTexCoorHandle();
        muHasTextureHandle = getHasTextureHandle();
        muLightLocationHandle = getLightLocationHandle();//光源位置句柄
        muCameraLocationHandle = getCameraLocationHandle();//摄像机位置句柄
        LogHelper.d(TAG, LogHelper.getThreadName() +
                " muMVPMatrixHandle=" + muMVPMatrixHandle +
                " muModelMatrixHandle=" + muModelMatrixHandle +
                " muViewMatrixHandle=" + muViewMatrixHandle +
                " muModelViewMatrixHandle=" + muModelViewMatrixHandle +
                " maPositionHandle=" + maPositionHandle +
                " maNormalHandle=" + maNormalHandle +
                " maTexCoorHandle=" + maTexCoorHandle +
                " muTextureHandle=" + muTextureHandle +
                " muHasTexCoorHandle=" + muHasTexCoorHandle +
                " muHasTextureHandle=" + muHasTextureHandle +
                " muLightLocationHandle=" + muLightLocationHandle +
                " muCameraLocationHandle=" + muCameraLocationHandle
        );
    }

    protected int getMVPMatrixHandle() {
        return getMaterialHandler("uMVPMatrix");
    }

    protected int getModelMatrixHandle() {
        return getMaterialHandler("uModelMatrix");
    }

    protected int getViewMatrixHandle() {
        return getMaterialHandler("uViewMatrix");
    }

    protected int getModelViewMatrixHandle() {
        return getMaterialHandler("uModelViewMatrix");
    }

    protected int getPositionHandle() {
        return getMaterialHandler("aPosition");
    }

    protected int getNormalHandle() {
        return getMaterialHandler("aNormal");
    }

    protected int getTexCoorHandle() {
        return getMaterialHandler("aTexCoor");
    }

    protected int getTextureHandle() {
        return getMaterialHandler("uTexture");
    }

    protected int getHasTexCoorHandle() {
        return getMaterialHandler("uHasTexCoor");
    }

    protected int getHasTextureHandle() {
        return getMaterialHandler("uHasTexture");
    }

    protected int getLightLocationHandle() {
        return getMaterialHandler("uLightLocation");
    }

    protected int getCameraLocationHandle() {
        return getMaterialHandler("uCameraLocation");
    }

    @Override
    public void onDrawFrame() {
        if (!isDataOK) {//如果数据都还没好就onDrawFrame，就会调用到onSurfaceCreated，此时mVertexBuffer还是null
            return;
        }
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        super.onDrawFrame();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        if (muHasTexCoorHandle != -1) {
            GLES20.glUniform1f(muHasTexCoorHandle, mMatHasTexCoor);
        }
        if (muHasTextureHandle != -1) {
            GLES20.glUniform1f(muHasTextureHandle, mMatHasTexture);
        }
        // 将最终变换矩阵传入着色器程序
        if (muMVPMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getMVPMatrix(), 0);
        }
        if (muModelMatrixHandle != -1) {
            // 将位置、旋转变换矩阵传入着色器程序
            GLES20.glUniformMatrix4fv(muModelMatrixHandle, 1, false, getModelMatrix(), 0);
        }
        if (muViewMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muViewMatrixHandle, 1, false, getViewMatrix(), 0);
        }
        if (muModelViewMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muModelViewMatrixHandle, 1, false, getModelViewMatrix(), 0);
        }
        VBOHelper.drawVBOs(mVBOOptions);
        if (muLightLocationHandle != -1) {
            GLES20.glUniform3fv(muLightLocationHandle, 1, mLightLocationBuffer);
        }
        if (muCameraLocationHandle != -1) {
            GLES20.glUniform3fv(muCameraLocationHandle, 1, mCameraLocationBuffer);
        }
        onDrawArraysPre();
        // 绘制加载的物体
        GLES20.glDrawArrays(mObjViewData.primitiveMode,//GLES20.GL_TRIANGLES,希望绘制的是一个三角面;GLES20.GL_LINE_STRIP,三角形
                0, //顶点数组的起始索引
                mVertexCount//绘制多少个顶点
        );
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
        if (muTextureHandle != -1 && mTextureID != -1) {//之所以放这里是因为可能子类需要绑定不同的纹理ID，
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(muTextureHandle, 0);
        }
    }

    protected void onDrawArraysAfter() {
    }

    @Override
    public void onDestroy() {
        if (mVertexBuffer != null) {
            mVertexBuffer.clear();
            mVertexBuffer = null;
        }
        if (mNormalBuffer != null) {
            mNormalBuffer.clear();
            mNormalBuffer = null;
        }
        if (mTexCoorBuffer != null) {
            mTexCoorBuffer.clear();
            mTexCoorBuffer = null;
        }
        super.onDestroy();
    }

    @Override
    public void setTexture(Bitmap bitmap, TextureOptions options, boolean recycleBmp) {
        mMatHasTexture = 1f;
        super.setTexture(bitmap, options, recycleBmp);
    }

    @Override
    public void initTexture(int textureId) {
        mMatHasTexture = 1f;
        super.initTexture(textureId);
    }

    protected void setLightLocation(Vec3 vec) {
        mLightLocationBuffer = ArrayToBufferHelper.floatArrayToBuffer(vec.toFloatArray());
    }

    protected void setCameraLocation(Vec3 vec) {
        mCameraLocationBuffer = ArrayToBufferHelper.floatArrayToBuffer(vec.toFloatArray());
    }

    public ObjViewData getObjViewData() {
        return mObjViewData;
    }
}
