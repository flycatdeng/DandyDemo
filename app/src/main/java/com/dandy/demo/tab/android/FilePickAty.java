package com.dandy.demo.tab.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dandy.demo.tab.BaseDemoAty;
import com.dandy.helper.android.LogHelper;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng
 * Wechat: flycatdeng
 * Emial: flycatdeng@qq.com
 */

public class FilePickAty extends BaseDemoAty {
    private static final int ACTIVITY_REQUEST_CODE_File_PICK = 0;
    private RelativeLayout mRootView;
    private Button mBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new RelativeLayout(this);
        setContentView(mRootView);
        mBtn = new Button(this);
        mBtn.setId(View.generateViewId());
        mBtn.setText("Choose File");
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), ACTIVITY_REQUEST_CODE_File_PICK);
                } catch (android.content.ActivityNotFoundException ex) {
                    LogHelper.showToast(FilePickAty.this, "Please install a File Manager.");
                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.addView(mBtn, lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTIVITY_REQUEST_CODE_File_PICK == requestCode && resultCode == RESULT_OK && null != data) {
            //获取返回的数据，这里是android自定义的Uri地址
            Uri uri = data.getData();
            String path = getPath(this, uri);
            if (path == null) {
                LogHelper.showToast(this, "path is null");
            } else {
                LogHelper.showToast(this, "path=" + path);
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
