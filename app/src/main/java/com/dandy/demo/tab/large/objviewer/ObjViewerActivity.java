package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dandy.demo.R;
import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.helper.android.LogHelper;

public class ObjViewerActivity extends BaseDemoAty implements View.OnClickListener {
    private static final String TAG = "ObjViewerActivity";
    private Context mContext;
    private FrameLayout mContentLayout;
    private LinearLayout mMenuLayout;
    private ContentView mContentView;
    private ImageView mPrimitiveModeImg;//图元模式
    private ImageView mProjectionImg;//投影模式
    private ObjViewData mObjViewData;
    private IDataChangeListener mDataChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.tab_large_objviewer_aty_objviewer);
        findViews();
        mContentView = new ContentView(mContext);
        mContentView.setFocusable(true);
        mContentView.setClickable(true);
        mContentLayout.addView(mContentView);
        mContentView.setOnDataLoadListener(new ContentView.OnDataLoadListener() {
            @Override
            public void onDataOK(ObjViewData data, IDataChangeListener listener) {
                mObjViewData = data;
                mDataChangeListener = listener;
//                mMenuLayout.setVisibility(View.VISIBLE);
//                mMenuLayout.bringToFront();
                setClickListeners();
            }

            @Override
            public void onDataFailed() {
                LogHelper.d(TAG, LogHelper.getThreadName());
            }
        });
    }

    private void findViews() {
        mContentLayout = (FrameLayout) findViewById(R.id.tab_large_objview_aty_content);
        mMenuLayout = (LinearLayout) findViewById(R.id.tab_large_objview_aty_menu);
        mPrimitiveModeImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_primitive_mode);
        mPrimitiveModeImg.setTag(R.drawable.objview_ball);
        mProjectionImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_projection);
        mProjectionImg.setTag(R.drawable.objview_projection_perspective);
    }

    private void setClickListeners() {
        mPrimitiveModeImg.setOnClickListener(this);
        mProjectionImg.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_large_objview_aty_menu_primitive_mode:
                boolean isPrimitiveModeTriangle = ((int) mPrimitiveModeImg.getTag()) == R.drawable.objview_ball;//之前的图元是三角面吗？
                if (isPrimitiveModeTriangle) {
                    mPrimitiveModeImg.setImageResource(R.drawable.objview_ball_wireframe);
                    mPrimitiveModeImg.setTag(R.drawable.objview_ball_wireframe);
                    mObjViewData.primitiveMode = GLES20.GL_LINE_STRIP;
                } else {
                    mPrimitiveModeImg.setImageResource(R.drawable.objview_ball);
                    mPrimitiveModeImg.setTag(R.drawable.objview_ball);
                    mObjViewData.primitiveMode = GLES20.GL_TRIANGLES;
                }
                mDataChangeListener.onRenderValueChanged(mObjViewData);
                break;
            case R.id.tab_large_objview_aty_menu_projection:
                boolean isProjectionPerspective = ((int) mProjectionImg.getTag()) == R.drawable.objview_projection_perspective;//之前的投影模式是透视吗？
                LogHelper.d(TAG, LogHelper.getThreadName() + " isProjectionPerspective=" + isProjectionPerspective);
                if (isProjectionPerspective) {
                    mProjectionImg.setImageResource(R.drawable.objview_projection_ortho);
                    mProjectionImg.setTag(R.drawable.objview_projection_ortho);
                    mObjViewData.projectMode = ObjViewData.ProjectMode.ORTHO;
                } else {
                    mProjectionImg.setImageResource(R.drawable.objview_projection_perspective);
                    mProjectionImg.setTag(R.drawable.objview_projection_perspective);
                    mObjViewData.projectMode = ObjViewData.ProjectMode.PERSPECTIVE;
                }
                mDataChangeListener.onProjectionChanged(mObjViewData);
                break;
            default:
                break;
        }
    }
}
