package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
    private float mMinValue = 0.0f;
    private float mMaxValue = 1.0f;
    private float mCurValue = 0.5f;
    private static final int PROGRESS_DELTA = 100;

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

    public static LightCtrlItemView fromXml(Context context, OnValueChangedListener listener) {
        LightCtrlItemView view = (LightCtrlItemView) View.inflate(context, R.layout.tab_large_objviewer_view_item_light_control, null);
        view.setOnValueChangedListener(listener);
        return view;
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
        setListeners();
    }

    private void setListeners() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                float curValue = (mMaxValue - mMinValue) * progress / PROGRESS_DELTA;
                mEditText.setText(curValue + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mMinusTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float delta = (mMaxValue - mMinValue) / PROGRESS_DELTA;
                float newValue = mCurValue - delta;
                if (newValue < mMinValue) {
                    LogHelper.showToast(mContext, "不能少于最小值：" + mMinValue);
                    return;
                }
                setValue(newValue);
            }
        });
        mPlusTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float delta = (mMaxValue - mMinValue) / PROGRESS_DELTA;
                float newValue = mCurValue + delta;
                if (newValue > mMaxValue) {
                    LogHelper.showToast(mContext, "不能大于最大值：" + mMaxValue);
                    return;
                }
                setValue(newValue);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogHelper.d(TAG, LogHelper.getThreadName());
                float value = 0.0f;
                try {
                    value = Float.valueOf(mEditText.getText().toString());
                } catch (Exception e) {
                    LogHelper.showToast(mContext, "e=" + e.getMessage());
                    setValue(mCurValue);
                    return;
                }
                LogHelper.d(TAG, LogHelper.getThreadName() + " value=" + value);
                if (value < mMinValue) {
                    setValue(mMinValue);
                } else if (value > mMaxValue) {
                    setValue(mMaxValue);
                } else {
                    setValue(value);
                }
            }
        });
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

    public void setValueRange(float minValue, float maxValue) {
        mMinValue = minValue;
        mMaxValue = maxValue;
    }

    public void setValue(float value) {
        LogHelper.d(TAG, LogHelper.getThreadName() + " value=" + value+" mCurValue="+mCurValue);
        if (Math.abs(mCurValue - value) < 0.0000001f) {
            return;
        }
        mCurValue = value;
        mEditText.setText(mCurValue + "");
        int progress = (int) (value / (mMaxValue - mMinValue) * PROGRESS_DELTA);
        LogHelper.d(TAG, LogHelper.getThreadName() + " progress=" + progress);
        mSeekBar.setProgress(progress);
        if (mOnValueChangedListener != null) {
            mOnValueChangedListener.onValueChanged(value);
        }
    }

    private OnValueChangedListener mOnValueChangedListener;

    private void setOnValueChangedListener(OnValueChangedListener listener) {
        mOnValueChangedListener = listener;
    }

    interface OnValueChangedListener {
        void onValueChanged(float value);
    }
}
