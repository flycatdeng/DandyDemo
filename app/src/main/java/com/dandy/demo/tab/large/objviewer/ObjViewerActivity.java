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
    private ImageView mPrimitiveModeIV;
    private ObjViewData mObjViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.tab_large_objviewer_aty_objviewer);
        mContentLayout = (FrameLayout) findViewById(R.id.tab_large_objview_aty_content);
        mMenuLayout = (LinearLayout) findViewById(R.id.tab_large_objview_aty_menu);
        mPrimitiveModeIV = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_primitive_mode);
        mPrimitiveModeIV.setTag(R.drawable.objview_ball);
        mContentView = new ContentView(mContext);
        mContentView.setFocusable(true);
        mContentView.setClickable(true);
        mContentLayout.addView(mContentView);
        mContentView.setOnDataLoadListener(new ContentView.OnDataLoadListener() {
            @Override
            public void onDataOK(ObjViewData data) {
                mObjViewData = data;
//                mMenuLayout.setVisibility(View.VISIBLE);
//                mMenuLayout.bringToFront();
                mPrimitiveModeIV.setOnClickListener(ObjViewerActivity.this);
            }

            @Override
            public void onDataFailed() {
                LogHelper.d(TAG, LogHelper.getThreadName());
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_large_objview_aty_menu_primitive_mode:
                boolean isPrimitiveModeTriangle = ((int) mPrimitiveModeIV.getTag()) == R.drawable.objview_ball;
                if (isPrimitiveModeTriangle) {
                    mPrimitiveModeIV.setImageResource(R.drawable.objview_ball_wireframe);
                    mPrimitiveModeIV.setTag(R.drawable.objview_ball_wireframe);
                    mObjViewData.primitiveMode = GLES20.GL_LINE_STRIP;
                } else {
                    mPrimitiveModeIV.setImageResource(R.drawable.objview_ball);
                    mPrimitiveModeIV.setTag(R.drawable.objview_ball);
                    mObjViewData.primitiveMode = GLES20.GL_TRIANGLES;
                }
                mContentView.requestRender();
                break;
            default:
                break;
        }
    }
}
