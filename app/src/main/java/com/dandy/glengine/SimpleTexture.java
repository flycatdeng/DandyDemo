package com.dandy.glengine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.java.nio.ArrayToBufferHelper;

import java.nio.FloatBuffer;

/**
 * <pre>
 *     直接显示纹理的方式，无需借助Matrix的一种方式，因为这里没有用到坐标变换
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class SimpleTexture extends Actor {
    private static final String TAG = "SimpleTexture";
    protected FloatBuffer mPositionBuffer;
    protected FloatBuffer mTexCoorBuffer;
    protected int aPositionHandler = -1;
    protected int aTexcoorHandler = -1;
    protected int uSamplerTextureHandler = -1;
    protected static final float POSITION[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    protected static final float TEXTURE[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    protected boolean mIsInitialized;

    public SimpleTexture(Context context) {
        super(context);
        mDefaultTextureOptions = null;
        mPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(POSITION);
        mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(TEXTURE);
        mDefaultMaterialName = "gles_engine_simple_image/simple.mat";
    }

    public void onDestroy() {
        mIsInitialized = false;
        if (mPositionBuffer != null) {
            mPositionBuffer.clear();
            mPositionBuffer = null;
        }
        if (mTexCoorBuffer != null) {
            mTexCoorBuffer.clear();
            mTexCoorBuffer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        if (aPositionHandler == -1) {
            aPositionHandler = getMaterialHandler("position");
        }
        GLES20.glVertexAttribPointer(aPositionHandler,//指定要修改的顶点属性的索引值,句柄
                2,//指定每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））,这里我们没有用到Z轴
                GLES20.GL_FLOAT, //指定数组中每个组件的数据类型
                false, //指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）
                0, //stride 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0,这里填的2*4也一样的效果
                mPositionBuffer//指定第一个组件在数组的第一个顶点属性中的偏移量。该数组与GL_ARRAY_BUFFER绑定，储存于缓冲区中。初始值为0
        );
        GLES20.glEnableVertexAttribArray(aPositionHandler);

        if (aTexcoorHandler == -1) {
            aTexcoorHandler = getMaterialHandler("inputTextureCoordinate");
        }
        GLES20.glVertexAttribPointer(aTexcoorHandler, 2, GLES20.GL_FLOAT, false, 0, mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(aTexcoorHandler);

        if (uSamplerTextureHandler == -1) {
            uSamplerTextureHandler = getMaterialHandler("inputImageTexture");
        }
        if (mTextureID != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(uSamplerTextureHandler, 0);
        }

        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
        GLES20.glDisableVertexAttribArray(aPositionHandler);
        GLES20.glDisableVertexAttribArray(aTexcoorHandler);
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }
}
