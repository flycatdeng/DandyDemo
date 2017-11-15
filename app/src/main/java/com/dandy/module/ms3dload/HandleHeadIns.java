package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;

import java.io.IOException;

/**
 * <pre>
 *     处理头文件信息，长度为14个字节
 *     前10字节为固定字符串"MS3D00000"；
 *     后4个字节为有符号整型，表示该版本号，一般为3或4
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleHeadIns extends HandleInsChain {
    private static final String TAG = "HandleHeadIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        //从指向ms3d文件的流中读取文件头信息并生成MS3DHeader对象
        MS3DHeader pojo = new MS3DHeader(fis.readString(10), fis.readInt());
        data.setHeader(pojo);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + pojo.toString());
        }
    }
}
