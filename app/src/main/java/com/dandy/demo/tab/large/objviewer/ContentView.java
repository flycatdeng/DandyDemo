package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.dandy.glengine.Stage;
import com.dandy.glengine.android.StageView;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.android.res.SDCardHelper;
import com.dandy.helper.java.math.Vec3;
import com.dandy.module.obj3dload.ActorObject3D;
import com.dandy.module.obj3dload.Obj3DBufferLoadAider;
import com.dandy.module.obj3dload.Obj3DLoadAider;
import com.dandy.module.obj3dload.Obj3DLoadResult;
import com.dandy.module.obj3dload.OnLoadListener;
import com.dandy.module.touchzoom.TouchZoomAider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/11/28
 */
public class ContentView extends FrameLayout {
    private static final String TAG = "ContentView";
    private Context mContext;
    private StageView mStageView;
    private Stage mStage;
    private ActorObjView3D mActor;
    private TouchZoomAider mTouchZoomAider;
    private ObjViewData mObjViewData = ObjViewData.getInstance();

    public ContentView(Context context) {
        super(context);
        mContext = context;
        mTouchZoomAider = new TouchZoomAider();
        mTouchZoomAider.setThreshold(10);
        initStageView();
        initObjData(AssetsHelper.getInputStream(mContext, "demo/obj/changgui.obj"));
        mTouchZoomAider.setTouchZoomListener(new TouchZoomAider.TouchZoomListenerAdapter() {
            @Override
            public void zoomOut(float scale) {
                mActor.scale(scale);
                mObjViewData.scale *= scale;
            }

            @Override
            public void zoomIn(float scale) {
                mActor.scale(scale);
                mObjViewData.scale *= scale;
            }
        });
    }

    private void initStageView() {
        mStageView = new StageView(mContext);
        mStageView.initRenderer();
        mStage = mStageView.getStage();
        addView(mStageView);
    }

    private void initObjData(InputStream objIns) {
        new Obj3DLoadAider().loadFromInputStreamAsync(objIns,
                new OnLoadListener() {

                    @Override
                    public void onLoadOK(Obj3DLoadResult result) {
                        LogHelper.d(TAG, LogHelper.getThreadName() + " Obj3DLoadResult=" + result.toString());
                        mActor = new ActorObjView3D(mContext);
                        mActor.setTexture(AssetsHelper.getBitmap(mContext, "demo/obj/head.jpg"));
                        mActor.loadFromData(result.getVertexXYZ(), result.getNormalVectorXYZ(), result.getTextureVertexST());
                        mStage.add(mActor);
                        mActor.scale(mObjViewData.scale);
                        mActor.requestRender();
                        if (mOnDataLoadListener != null) {
                            mOnDataLoadListener.onDataOK(mActor.getObjViewData(), mActor);
                        }
                    }

                    @Override
                    public void onLoadFailed(String failedMsg) {
                        LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
                        if (mOnDataLoadListener != null) {
                            mOnDataLoadListener.onDataFailed(" failedMsg=" + failedMsg);
                        }
                    }
                }
        );
    }

    private void initObjBufferData(final String vertexFilePath, final String normalFilePath, final String texcoorFilePath) {
        new Obj3DBufferLoadAider().loadFromPathAsyn(vertexFilePath, normalFilePath, texcoorFilePath, new OnLoadListener() {

            @Override
            public void onLoadOK(Obj3DLoadResult result) {
                LogHelper.d(TAG, LogHelper.getThreadName() + " Obj3DLoadResult=" + result.toString());
                mActor = new ActorObjView3D(mContext);
                mActor.setTexture(AssetsHelper.getBitmap(mContext, "demo/obj/head.jpg"));
                mActor.loadFromData(result.getVertexXYZ(), result.getNormalVectorXYZ(), result.getTextureVertexST());
                mStage.add(mActor);
                ObjViewData data = ObjViewData.getInstance();
                mActor.scale(data.scale);
                mActor.requestRender();
                if (mOnDataLoadListener != null) {
                    mOnDataLoadListener.onDataOK(mActor.getObjViewData(), mActor);
                }
            }

            @Override
            public void onLoadFailed(String failedMsg) {
                LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
                if (mOnDataLoadListener != null) {
                    mOnDataLoadListener.onDataFailed(" failedMsg=" + failedMsg);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouchZoomAider.dealTouchZoom(ev);
        mActor.requestRender();
        return super.dispatchTouchEvent(ev);
    }

    public void changeObjFileFromSDCard(String path) {
        removeView(mStageView);
        try {
            initStageView();
            initObjData(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (mOnDataLoadListener != null) {
                mOnDataLoadListener.onDataFailed(e.getMessage());
            }
        }
    }

    public void changeObjBufferFileFromSDCard(String dir) {
        File dirFile = new File(dir);
        String vertexFilePath = "";
        String normalFilePath = "";
        String texcoorFilePath = "";
        if (dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            if (files == null || files.length == 0) {
                LogHelper.showToastOnUIThread(mContext, "files == null || files.length == 0");
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getAbsolutePath().endsWith(".nxyz")) {
                    normalFilePath = files[i].getAbsolutePath();
                }
                if (files[i].getAbsolutePath().endsWith(".vxyz")) {
                    vertexFilePath = files[i].getAbsolutePath();
                }
                if (files[i].getAbsolutePath().endsWith(".tst")) {
                    texcoorFilePath = files[i].getAbsolutePath();
                }
            }
            if (TextUtils.isEmpty(vertexFilePath) || TextUtils.isEmpty(normalFilePath) || TextUtils.isEmpty(texcoorFilePath)) {
                LogHelper.showToastOnUIThread(mContext, "您所选择的目录下必须同时存在nxyz/vxyz/tst三个文件");
                return;
            }
            removeView(mStageView);
            initStageView();
            initObjBufferData(vertexFilePath, normalFilePath, texcoorFilePath);
        } else {
            LogHelper.showToastOnUIThread(mContext, "传参需要是文件夹");
        }
    }

    public interface OnDataLoadListener {
        void onDataOK(ObjViewData data, IDataChangeListener listener);

        void onDataFailed(String msg);
    }

    private OnDataLoadListener mOnDataLoadListener;

    public void setOnDataLoadListener(OnDataLoadListener listener) {
        mOnDataLoadListener = listener;
    }
}
