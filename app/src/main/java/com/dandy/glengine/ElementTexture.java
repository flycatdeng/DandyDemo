package com.dandy.glengine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.nio.ArrayToBufferHelper;

public class ElementTexture extends Actor {
    private static final String TAG = "ElementTexture";
    private static final short DRAW_ORDER[] = {0, 1, 2, 0, 2, 3};
    private static float TEX_COOR[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f};
    /**
     * the ratio of width and height
     */
    private float mWHScale = 1.0f;
    protected float squareSize_w = 1.0f;
    protected float squareSize_h = 1.0f;
    protected float vertices[];
    protected FloatBuffer mPositionBuffer;
    protected FloatBuffer mTexCoorBuffer;
    protected ShortBuffer mDrawOrderBuffer;
    protected int aPositionHandle = -1;
    protected int aTexCoorHandle = -1;
    protected int uTextureHandle = -1;

    public ElementTexture(Context context) {
        super(context);
        mDefaultTextureOptions = null;
        mDefaultMaterialName = "gles_engine_texture_element/shader.mat";
        addRunOnceBeforeDraw(new Runnable() {

            @Override
            public void run() {
                LogHelper.d(TAG,LogHelper.getThreadName()+"");
                vertices = new float[]{//
                        -squareSize_w * mWHScale, squareSize_h, 0.0f, // top left
                        -squareSize_w * mWHScale, -squareSize_h, 0.0f, // bottom// left
                        squareSize_w * mWHScale, -squareSize_h, 0.0f, // bottom// right
                        squareSize_w * mWHScale, squareSize_h, 0.0f}; // top right
                mVertexCount = 4;
                mPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertices);
                mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(TEX_COOR);
                mDrawOrderBuffer = ArrayToBufferHelper.shortArrayToBuffer(DRAW_ORDER);
            }
        });
    }

    @Override
    protected void onShaderLocationInit() {
        super.onShaderLocationInit();
        aPositionHandle = getMaterialHandler("aPosition");
        aTexCoorHandle = getMaterialHandler("aTexCoor");
        uTextureHandle = getMaterialHandler("uTexture");
    }

    /**
     * <pre>
     *      set the ratio of width and height
     * </pre>
     *
     * @param scale
     */
    public void setWHScale(float scale) {
        mWHScale = scale;
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        LogHelper.d(TAG,LogHelper.getThreadName()+" aPositionHandle="+aPositionHandle+" aTexCoorHandle="+
                aTexCoorHandle+" uTextureHandle="+uTextureHandle+" mTextureID="+mTextureID+" mVertexCount="+mVertexCount);
        if (aPositionHandle != -1) {
            GLES20.glVertexAttribPointer(aPositionHandle,//指定要修改的顶点属性的索引值,句柄
                    3,//指定每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））,这里我们没有用到Z轴
                    GLES20.GL_FLOAT, //指定数组中每个组件的数据类型
                    false, //指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）
                    0, //stride 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0,这里填的2*4也一样的效果
                    mPositionBuffer//指定第一个组件在数组的第一个顶点属性中的偏移量。该数组与GL_ARRAY_BUFFER绑定，储存于缓冲区中。初始值为0
            );
            GLES20.glEnableVertexAttribArray(aPositionHandle);
        }
        if (aTexCoorHandle != -1) {//如果TEX_COOR不要后面的0f 1f则填写2
            GLES20.glVertexAttribPointer(aTexCoorHandle, 4, GLES20.GL_FLOAT, false, 0, mTexCoorBuffer);
            GLES20.glEnableVertexAttribArray(aTexCoorHandle);
        }
        if (uTextureHandle != -1 && mTextureID != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(uTextureHandle, 0);
        }

        onDrawArraysPre();
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mVertexCount * 3 / 2, GLES20.GL_UNSIGNED_SHORT, mDrawOrderBuffer);
        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glDisableVertexAttribArray(aTexCoorHandle);
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }
}
