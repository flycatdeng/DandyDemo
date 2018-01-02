package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dandy.demo.R;
import com.dandy.demo.tab.BaseDemoAty;

public class ObjViewerActivity extends BaseDemoAty {
    private static final String TAG = "ObjViewerActivity";
    private Context mContext;
    private FrameLayout mContentLayout;
    private LinearLayout mSidebarLayout;
    private ContentView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.tab_large_objviewer_aty_objviewer);
        mContentLayout = (FrameLayout) findViewById(R.id.tab_large_objview_aty_content);
        mSidebarLayout = (LinearLayout) findViewById(R.id.tab_large_objview_aty_sidebar);
//        mSidebarLayout.addView(View.inflate(mContext, R.layout.tab_large_objviewer_view_sidebar, null));
        mContentView = new ContentView(mContext);
        mContentView.setFocusable(true);
        mContentView.setClickable(true);
        mContentLayout.addView(mContentView);
    }

}
