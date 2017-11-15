package com.dandy.demo.tab.opengl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.opengl.GLES20;
import android.view.animation.BounceInterpolator;

import com.dandy.glengine.Stage;
import com.dandy.glengine.android.ISimpleGLContent;
import com.dandy.glengine.android.SimpleGLActivity;
import com.dandy.glengine.android.SimpleGLContentAdapter;
import com.dandy.glengine.android.StageView;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.java.math.Vec4;
import com.dandy.helper.java.nio.ArrayToBufferHelper;
import com.dandy.module.obj3dload.ActorObject3D;
import com.dandy.module.obj3dload.Obj3DLoadAider;
import com.dandy.module.obj3dload.Obj3DLoadResult;
import com.dandy.module.obj3dload.OnLoadListener;

import java.nio.FloatBuffer;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class FaceObjMixAnimAty extends SimpleGLActivity {
    @Override
    public ISimpleGLContent getSimpleGLContent(Context context) {
        return new MySimpleGLContent(context);
    }

    class MySimpleGLContent extends SimpleGLContentAdapter {
        private static final String TAG = "FaceObjMixAnimAty.MySimpleGLContent";
        private Context mContext;
        private FaceObjMix3DActor faceObjActor;

        public MySimpleGLContent(Context context) {
            mContext = context;
            faceObjActor = new FaceObjMix3DActor(context);
            faceObjActor.scale(0.3f, 0.3f, 0.3f);
            faceObjActor.rotate(0f, 0f, 1f, 0f);
        }

        @Override
        public void onCreate(final Stage stage) {
            super.onCreate(stage);
            loadRes(stage);
        }

        private int mLoadResCount = 0;
        private int mLoadResSucceedCount = 0;
        private Obj3DLoadResult changguiResult;
        private Obj3DLoadResult kaikouResult;
        private Obj3DLoadResult piezuiResult;
        private Obj3DLoadResult youpiezuiResult;

        private void loadRes(final Stage stage) {
            new Obj3DLoadAider().loadFromInputStreamAsync(
                    AssetsHelper.getInputStream(mContext, "demo/facemixobj/changgui.obj"),
                    new OnLoadListener() {
                        @Override
                        public void onLoadOK(Obj3DLoadResult result) {
                            changguiResult = result;
                            onLoadResResult(stage, 1);
                        }

                        @Override
                        public void onLoadFailed(String failedMsg) {
                            onLoadResResult(stage, 0);
                        }
                    });
            new Obj3DLoadAider().loadFromInputStreamAsync(
                    AssetsHelper.getInputStream(mContext, "demo/facemixobj/kaikou.obj"),
                    new OnLoadListener() {
                        @Override
                        public void onLoadOK(Obj3DLoadResult result) {
                            kaikouResult = result;
                            faceObjActor.setKaikouVertex(result.getVertexXYZ());
                            onLoadResResult(stage, 1);
                        }

                        @Override
                        public void onLoadFailed(String failedMsg) {
                            onLoadResResult(stage, 0);
                        }
                    });
            new Obj3DLoadAider().loadFromInputStreamAsync(
                    AssetsHelper.getInputStream(mContext, "demo/facemixobj/piezui.obj"),
                    new OnLoadListener() {
                        @Override
                        public void onLoadOK(Obj3DLoadResult result) {
                            piezuiResult = result;
                            faceObjActor.setPiezuiVertex(result.getVertexXYZ());
                            onLoadResResult(stage, 1);
                        }

                        @Override
                        public void onLoadFailed(String failedMsg) {
                            onLoadResResult(stage, 0);
                        }
                    });
            new Obj3DLoadAider().loadFromInputStreamAsync(
                    AssetsHelper.getInputStream(mContext, "demo/facemixobj/youpiezui.obj"),
                    new OnLoadListener() {
                        @Override
                        public void onLoadOK(Obj3DLoadResult result) {
                            youpiezuiResult = result;
                            faceObjActor.setYouPiezuiVertex(result.getVertexXYZ());
                            onLoadResResult(stage, 1);
                        }

                        @Override
                        public void onLoadFailed(String failedMsg) {
                            onLoadResResult(stage, 0);
                        }
                    });
        }

        private synchronized void onLoadResResult(Stage stage, int count) {
            mLoadResCount++;
            mLoadResSucceedCount += count;
            if (mLoadResCount == 4 && mLoadResSucceedCount == 4) {//成功
                LogHelper.d(TAG, LogHelper.getThreadName() + " load res succeed");
                faceObjActor.loadFromData(changguiResult.getVertexXYZ(), changguiResult.getNormalVectorXYZ(), changguiResult.getTextureVertexST());
                stage.add(faceObjActor);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (Float) animation.getAnimatedValue();//0~1
//                                mPositionWeight.add(new Vec4(-value, value, 0f, value));
//                                faceObjActor.setPositionWeight(new Vec4(1.0f-value, value, 0f, value));
                                faceObjActor.setPositionWeight(new Vec4(1.0f-value, value, value, 0));
                                faceObjActor.requestRender();
                            }
                        });
                        animator.setInterpolator(new BounceInterpolator());
                        animator.setDuration(5000);
                        animator.start();
                    }
                });
            } else if (mLoadResCount == 4 && mLoadResSucceedCount != 4) {//失败
                LogHelper.d(TAG, LogHelper.getThreadName() + " load res failed");
            }
        }
    }

    class FaceObjMix3DActor extends ActorObject3D {//常规作为默认顶点数据
        private int maKaikouPositionHandle = -1;
        private int maPiezuiPositionHandle = -1;
        private int maYoupiezuiPositionHandle = -1;
        private int muPositionWeightHandle = -1;
        private FloatBuffer mKaikouPositionBuffer;// 顶点坐标数据缓冲
        private FloatBuffer maPiezuiPositionBuffer;// 顶点坐标数据缓冲
        private FloatBuffer maYoupiezuiPositionBuffer;// 顶点坐标数据缓冲
        private Vec4 mPositionWeight = new Vec4(0.0f, 0.0f, 1.0f, 0.0f);//常规，开口，撇嘴，右撇嘴

        public FaceObjMix3DActor(Context context) {
            super(context);
            mDefaultMaterialName = "demo/facemixobj/facemixobj.mat";
        }

        @Override
        protected void onShaderLocationInit() {
            super.onShaderLocationInit();
            maKaikouPositionHandle = getMaterialHandler("aKaikouPosition");
            maPiezuiPositionHandle = getMaterialHandler("aPiezuiPosition");
            maYoupiezuiPositionHandle = getMaterialHandler("aYoupiezuiPosition");
            muPositionWeightHandle = getMaterialHandler("uPositionWeight");
        }

        @Override
        protected void onDrawArraysPre() {
            super.onDrawArraysPre();
            if (maKaikouPositionHandle != -1) {
                GLES20.glVertexAttribPointer(maKaikouPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mKaikouPositionBuffer);
                GLES20.glEnableVertexAttribArray(maKaikouPositionHandle);
            }
            if (maPiezuiPositionHandle != -1) {
                GLES20.glVertexAttribPointer(maPiezuiPositionHandle, 3, GLES20.GL_FLOAT, false, 0, maPiezuiPositionBuffer);
                GLES20.glEnableVertexAttribArray(maPiezuiPositionHandle);
            }
            if (maYoupiezuiPositionHandle != -1) {
                GLES20.glVertexAttribPointer(maYoupiezuiPositionHandle, 3, GLES20.GL_FLOAT, false, 0, maYoupiezuiPositionBuffer);
                GLES20.glEnableVertexAttribArray(maYoupiezuiPositionHandle);
            }
            if (muPositionWeightHandle != -1) {
                GLES20.glUniform4f(muPositionWeightHandle, mPositionWeight.x, mPositionWeight.y, mPositionWeight.z, mPositionWeight.w);
            }
        }

        @Override
        protected void onDrawArraysAfter() {
            super.onDrawArraysAfter();
            GLES20.glDisableVertexAttribArray(maKaikouPositionHandle);
            GLES20.glDisableVertexAttribArray(maPiezuiPositionHandle);
            GLES20.glDisableVertexAttribArray(maYoupiezuiPositionHandle);
        }

        public void setKaikouVertex(float[] vertex) {
            mKaikouPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertex);
        }

        public void setPiezuiVertex(float[] vertex) {
            maPiezuiPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertex);
        }

        public void setYouPiezuiVertex(float[] vertex) {
            maYoupiezuiPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertex);
        }

        public void setPositionWeight(Vec4 positionWeight) {
            mPositionWeight = positionWeight;
        }
    }
}
