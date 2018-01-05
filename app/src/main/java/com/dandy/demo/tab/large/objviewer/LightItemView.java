package com.dandy.demo.tab.large.objviewer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

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

public class LightItemView extends LinearLayout {
    private static final String TAG = "LightItemView";
    private Context mContext;
    private RadioButton mPointLightBtn;
    private RadioButton mDirectLightBtn;
    private Button mDeleteButton;
    private LightCtrlItemView mLightCtrlX;
    private LightCtrlItemView mLightCtrlY;
    private LightCtrlItemView mLightCtrlZ;
    private LightCtrlItemView mLightCtrlHJGQ;//环境光强
    private LightCtrlItemView mLightCtrlSSGQ;//散射光强
    private LightCtrlItemView mLightCtrlJMGQ;//镜面光强
    private LightCtrlItemView mLightCtrlJMCC;//镜面光粗糙度
    private ObjViewData.Light mLight = ObjViewData.getInstance().light;

    public LightItemView(Context context) {
        super(context);
        init(context);
    }

    public LightItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LogHelper.d(TAG, LogHelper.getThreadName());
    }

    public static LightItemView fromXml(Context context,LightDataChangeListener listener) {
        LightItemView view = (LightItemView) View.inflate(context, R.layout.tab_large_objviewer_view_item_light_item, null);
        view.setLightDataChangeListener(listener);
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogHelper.d(TAG, LogHelper.getThreadName());
        mPointLightBtn = (RadioButton) findViewById(R.id.tab_large_objview_view_item_light_item_radio_point_light);
        mDirectLightBtn = (RadioButton) findViewById(R.id.tab_large_objview_view_item_light_item_radio_direct_light);
        mDeleteButton = (Button) findViewById(R.id.tab_large_objview_view_item_light_item_delete);


        mLightCtrlX = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                LogHelper.d(TAG, LogHelper.getThreadName());
                mLight.lightPosX = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlX.setLab("X:");
        mLightCtrlX.setValueRange(0, 100);
        mLightCtrlY = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.lightPosZ = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlY.setLab("Y:");
        mLightCtrlY.setValueRange(0, 100);
        mLightCtrlZ = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.lightPosY = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlZ.setLab("Z:");
        mLightCtrlZ.setValueRange(0, 100);
        mLightCtrlHJGQ = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.ambentIntensity = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlHJGQ.setLab("环境光强");
        mLightCtrlSSGQ = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.diffuseIntensity = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlSSGQ.setLab("散射光强");
        mLightCtrlJMGQ = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.specularIntensity = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlJMGQ.setLab("镜面光强");
        mLightCtrlJMCC = LightCtrlItemView.fromXml(mContext, new LightCtrlItemView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mLight.specularShininess = value;
                if (mLightDataChangeListener != null) {
                    mLightDataChangeListener.OnLightChanged();
                }
            }
        });
        mLightCtrlJMCC.setLab("镜面粗糙");
        mLightCtrlJMCC.setValueRange(0, 100);
        addView(mLightCtrlX);
        addView(mLightCtrlY);
        addView(mLightCtrlZ);
        addView(mLightCtrlHJGQ);
        addView(mLightCtrlSSGQ);
        addView(mLightCtrlJMGQ);
        addView(mLightCtrlJMCC);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        LogHelper.d(TAG, LogHelper.getThreadName());
//        mPointLightBtn.setChecked(true);
    }

    public void setColor(int color) {
        mPointLightBtn.setTextColor(color);
        mDirectLightBtn.setTextColor(color);
        mDeleteButton.setTextColor(color);
        mLightCtrlX.setColor(color);
        mLightCtrlY.setColor(color);
        mLightCtrlZ.setColor(color);
        mLightCtrlHJGQ.setColor(color);
        mLightCtrlSSGQ.setColor(color);
        mLightCtrlJMGQ.setColor(color);
        mLightCtrlJMCC.setColor(color);
    }

    public void setLight(ObjViewData.Light light) {
        mLightCtrlX.setValue(light.lightPosX);
        mLightCtrlY.setValue(light.lightPosY);
        mLightCtrlZ.setValue(light.lightPosZ);
        mLightCtrlHJGQ.setValue(light.ambentIntensity);
        mLightCtrlSSGQ.setValue(light.diffuseIntensity);
        mLightCtrlJMGQ.setValue(light.specularIntensity);
        mLightCtrlJMCC.setValue(light.specularShininess);
    }

    private LightDataChangeListener mLightDataChangeListener;

    private void setLightDataChangeListener(LightDataChangeListener listener) {
        mLightDataChangeListener = listener;
    }

    interface LightDataChangeListener {
        void OnLightChanged();
    }
}
