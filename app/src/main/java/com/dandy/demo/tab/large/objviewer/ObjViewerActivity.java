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
import com.dandy.helper.java.basedata.StringHelper;
import com.dandy.module.obj3dload.Obj3DLoadResult;

public class ObjViewerActivity extends BaseDemoAty implements View.OnClickListener {
    private static final String TAG = "ObjViewerActivity";
    private Context mContext;
    private FrameLayout mContentLayout;
    private LinearLayout mMenuLayout;
    private ContentView mContentView;
    private ImageView mPrimitiveModeImg;//图元模式
    private ImageView mProjectionImg;//投影模式
    private ImageView mImageChooseImg;//选择纹理
    private ImageView mObjFileChooseImg;//Obj 文件选择
    private ImageView mObjBufferFileChooseImg;//Obj Buffer文件选择
    private ImageView mStatsImg;//数据统计按钮
    private ObjViewData mObjViewData;
    private IDataChangeListener mDataChangeListener;
    private static final int ACTIVITY_REQUEST_CODE_IMAGE_PICK = 0;
    private static final int ACTIVITY_REQUEST_CODE_FILE_PICK = 1;
    private static final int ACTIVITY_REQUEST_CODE_BUFFER_FILE_PICK = 2;
    private Obj3DLoadResult mObjModeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.tab_large_objviewer_aty_objviewer);
        findViews();
        initObjViewContent();
    }

    private void initObjViewContent() {
        mContentView = new ContentView(mContext);
        mContentView.setFocusable(true);
        mContentView.setClickable(true);
        mContentLayout.addView(mContentView);
        mContentView.setOnDataLoadListener(new ContentView.OnDataLoadListener() {
            @Override
            public void onDataOK(ObjViewData data, IDataChangeListener listener, Obj3DLoadResult result) {
                mObjViewData = data;
                mObjModeData = result;
                mDataChangeListener = listener;
                setImageClickable(true);
                setClickListeners();
            }

            @Override
            public void onDataFailed(String msg) {
                setImageClickable(true);
                LogHelper.d(TAG, LogHelper.getThreadName());
                LogHelper.showToastOnUIThread(mContext, LogHelper.getThreadName() + " " + msg);
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
        mObjFileChooseImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_objfile);
        mObjBufferFileChooseImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_buffer_objfile);
        mStatsImg = (ImageView) findViewById(R.id.tab_large_objview_aty_menu_stats);
    }

    private void setClickListeners() {
        mPrimitiveModeImg.setOnClickListener(this);
        mProjectionImg.setOnClickListener(this);
        mImageChooseImg.setOnClickListener(this);
        mObjFileChooseImg.setOnClickListener(this);
        mObjBufferFileChooseImg.setOnClickListener(this);
        mStatsImg.setOnClickListener(this);
    }

    private void setImageClickable(boolean clickable) {
        mPrimitiveModeImg.setClickable(clickable);
        mProjectionImg.setClickable(clickable);
        mImageChooseImg.setClickable(clickable);
        mObjFileChooseImg.setClickable(clickable);
        mObjBufferFileChooseImg.setClickable(clickable);
        mStatsImg.setClickable(clickable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_large_objview_aty_menu_primitive_mode://切换图元模式
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
            case R.id.tab_large_objview_aty_menu_projection://切换投影模式
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
            case R.id.tab_large_objview_aty_menu_image://选择纹理
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE_IMAGE_PICK);
                break;
            case R.id.tab_large_objview_aty_menu_objfile:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("*/*");
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent1, "Select a File to Upload"), ACTIVITY_REQUEST_CODE_FILE_PICK);
                } catch (android.content.ActivityNotFoundException ex) {
                    LogHelper.showToast(mContext, "Please install a File Manager.");
                }
                break;//ACTIVITY_REQUEST_CODE_BUFFER_FILE_PICK
            case R.id.tab_large_objview_aty_menu_buffer_objfile:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("*/*");
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent2, "Select a File to Upload"), ACTIVITY_REQUEST_CODE_BUFFER_FILE_PICK);
                } catch (android.content.ActivityNotFoundException ex) {
                    LogHelper.showToast(mContext, "Please install a File Manager.");
                }
                break;
            case R.id.tab_large_objview_aty_menu_stats:
                showStats();
                break;
            default:
                break;
        }
    }

    private void showStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoadTime= ").append(mObjModeData.getLoadTime()).append(" ms\n");
        sb.append("Triangle Faces= ").append(mObjModeData.getTrianglesCount()).append("\n");
        sb.append("Vertex num= ").append(mObjModeData.getVextexCount()).append("\n");
        sb.append("Normal num= ").append(mObjModeData.getNormalVectorCount()).append("\n");
        sb.append("TextureCoor num= ").append(mObjModeData.getTextureCoorCount()).append("\n");
        sb.append(mObjViewData.toString());
        LogHelper.showToast(mContext, sb.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTIVITY_REQUEST_CODE_IMAGE_PICK == requestCode && resultCode == RESULT_OK && null != data) {
            //获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //获取选择照片的数据视图
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            //从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mDataChangeListener.onTextureChanged(BitmapFactory.decodeFile(picturePath));
        } else if (ACTIVITY_REQUEST_CODE_FILE_PICK == requestCode && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            String path = getPath(this, uri);
            if (path == null) {
                LogHelper.showToast(this, "path is null");
            } else if (!path.endsWith(".obj")) {
                LogHelper.showToast(this, "必须是obj文件（以.obj结尾）");
            } else {
                setImageClickable(false);
                mContentView.changeObjFileFromSDCard(path);
            }
        } else if (ACTIVITY_REQUEST_CODE_BUFFER_FILE_PICK == requestCode && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            String path = getPath(this, uri);
            LogHelper.d(TAG, LogHelper.getThreadName() + " path=" + path);
            if (path == null) {
                LogHelper.showToast(this, "path is null");
            } else if ((!path.endsWith(".vxyz")) && (!path.endsWith(".nxyz")) && (!path.endsWith(".tst"))) {
                LogHelper.showToast(this, "必须是vxyz/nxyz/tst文件");
            } else {
                String dir = StringHelper.getParentDirectory(path);
                setImageClickable(false);
                mContentView.changeObjBufferFileFromSDCard(dir);
            }
        }
    }

    private String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
