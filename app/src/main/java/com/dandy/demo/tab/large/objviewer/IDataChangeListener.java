package com.dandy.demo.tab.large.objviewer;

import android.graphics.Bitmap;

/**
 * <pre>
 *
 * </pre>
 * Created by QueenJar
 * Wechat: queenjar
 * Emial: queenjar@qq.com
 */

public interface IDataChangeListener {
    void onProjectionChanged(ObjViewData data);

    void onRenderValueChanged(ObjViewData data);

    /**
     * 切换纹理，由于选择的图片大小可能不一致，所以这个方法会每次都重新创建一个纹理ID
     *
     * @param bitmap
     */
    void onTextureChanged(Bitmap bitmap);
}
