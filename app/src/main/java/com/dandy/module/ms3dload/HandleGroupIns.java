package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;

import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleGroupIns extends HandleInsChain {
    private static final String TAG = "HandleGroupIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        int count = fis.readUnsignedShort();//读取组数量
        //创建组信息对象数组
        MS3DGroup[] groups = new MS3DGroup[count];
        for (int i = 0; i < count; i++) {//循环加载每个组的信息
            byte flag = fis.readByte();  //标志--暂时无用，读了扔掉
            String name = fis.readString(32);//读取组名称--暂时无用，读了扔掉
            int indexCount = fis.readUnsignedShort();//读取组内三角形数量
            int[] triangleIndicies = new int[indexCount];//创建组内三角形索引数组
            for (int j = 0; j < indexCount; j++) { //加载组内各个三角形的索引
                triangleIndicies[j] = fis.readUnsignedShort();
            }
            byte materialIndex = fis.readByte();//读取材质索引，-1则表示本组不包含材质
            MS3DGroup group = new MS3DGroup(flag, name, triangleIndicies, materialIndex);
            groups[i] = group;
        }
        data.setGroups(groups);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "groups.length=" + groups.length);
        }
    }
}
