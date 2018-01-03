package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private ImageView mImageChooseImg;//选择纹理
    private ObjViewData mObjViewData;
    private IDataChangeListener mDataChangeListener;
    private static final int ACTIVITY_REQUEST_CODE_IMAGE_PICK = 0;

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
        mImageChooseImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_image);
    }

    private void setClickListeners() {
        mPrimitiveModeImg.setOnClickListener(this);
        mProjectionImg.setOnClickListener(this);
        mImageChooseImg.setOnClickListener(this);
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
            case R.id.tab_large_objview_aty_menu_image:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE_IMAGE_PICK);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTIVITY_REQUEST_CODE_IMAGE_PICK == requestCode && resultCode == RESULT_OK && null != data) {
            //获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //获取选择照片的数据视图
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            //从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mDataChangeListener.onTextureChanged(BitmapFactory.decodeFile(picturePath));
        }
    }
}
