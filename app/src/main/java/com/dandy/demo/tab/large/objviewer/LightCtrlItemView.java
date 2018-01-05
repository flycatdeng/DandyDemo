package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dandy.demo.R;
import com.dandy.helper.android.LogHelper;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng
 * Wechat: flycatdeng
 * Emial: flycatdeng@qq.com
 */

public class LightCtrlItemView extends LinearLayout {
    private static final String TAG = "LightCtrlItemView";
    private Context mContext;
    private TextView mLabTV;//
    private SeekBar mSeekBar;
    private TextView mMinusTV;
    private TextView mPlusTV;
    private EditText mEditText;

    public LightCtrlItemView(Context context) {
        super(context);
        init(context);
    }

    public LightCtrlItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LogHelper.d(TAG, LogHelper.getThreadName());
    }

    public static LightCtrlItemView fromXml(Context context) {
        return (LightCtrlItemView) View.inflate(context, R.layout.tab_large_objviewer_view_item_light_control, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogHelper.d(TAG, LogHelper.getThreadName());
        mLabTV = (TextView) findViewById(R.id.tab_large_objview_view_item_light_control_lab);//
        mSeekBar = (SeekBar) findViewById(R.id.tab_large_objview_view_item_light_control_seekbar);//
        mMinusTV = (TextView) findViewById(R.id.tab_large_objview_view_item_light_control_minus);//
        mPlusTV = (TextView) findViewById(R.id.tab_large_objview_view_item_light_control_plus);//
        mEditText = (EditText) findViewById(R.id.tab_large_objview_view_item_light_control_edit);//
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

    }

    public void setClickable(boolean clickable) {
        mSeekBar.setClickable(clickable);
        mMinusTV.setClickable(clickable);
        mPlusTV.setClickable(clickable);
        mEditText.setClickable(clickable);
    }

    public void setColor(int color) {
        mLabTV.setTextColor(color);
        mMinusTV.setTextColor(color);
        mPlusTV.setTextColor(color);
        mEditText.setTextColor(color);
    }

    public void setLab(String lab) {
        mLabTV.setText(lab);
    }
}
