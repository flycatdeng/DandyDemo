package com.dandy.module.ms3dload;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.glengine.Actor;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.opengl.TextureOptions;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class ActorMS3D extends Actor {
    private static final String TAG = "ActorMS3D";
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id
    int maTexCoorHandle; //顶点纹理坐标属性引用id
    private boolean isDataOK = false;
    private float mCurrentTime = 0f;//当前动画时间点

    public ActorMS3D(Context context) {
        super(context);
        mDefaultTextureOptions = TextureOptions.defaultMipmapOptions();
        mDefaultMaterialName = "module/ms3d/ms3d.mat";
    }

    public void initData(MS3DData data) {
        LogHelper.d(TAG, LogHelper.getThreadName() + " data=" + data.toString());

        isDataOK = true;
        requestRender();
    }

    @Override
    protected void onShaderLocationInit() {
        super.onShaderLocationInit();
        muMVPMatrixHandle = getMaterialHandler("uMVPMatrix");
        maPositionHandle = getMaterialHandler("aPosition");
        maTexCoorHandle = getMaterialHandler("aTexCoor");
        LogHelper.d(TAG, LogHelper.getThreadName()
                + " muMVPMatrixHandle=" + muMVPMatrixHandle
                + " maPositionHandle=" + maPositionHandle
                + " maTexCoorHandle=" + maTexCoorHandle
        );
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(final int width, final int height) {
        super.onSurfaceChanged(width, height);
        addRunOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glViewport(0, 0, width, height);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }
        });
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
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,//希望绘制的是一个三角形
//                0, //顶点数组的起始索引
//                3//绘制多少个顶点
//        );
    }
}
