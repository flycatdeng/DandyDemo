package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;
import com.dandy.helper.java.math.Vec3;

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

public class HandleVertexIns extends HandleInsChain {
    private static final String TAG = "HandleVertexIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        //读取顶点信息
        int count = fis.readUnsignedShort();//读取顶点数量
        MS3DVertex[] vertexs = new MS3DVertex[count];//创建顶点信息对象数组
        for (int i = 0; i < count; i++) {//循环读取每个顶点的信息
            byte flagFirst = fis.readByte();  //标志--暂时无用，读了扔掉
            Vec3 initPosition = new Vec3(fis.readFloat(), fis.readFloat(), fis.readFloat());//顶点XYZ坐标
            byte bone = fis.readByte();//骨骼ID
            byte flagLast = fis.readByte();  //标志--暂时无用，读了扔掉
            MS3DVertex vertex = new MS3DVertex(flagFirst, initPosition, bone, flagLast);
            vertexs[i] = vertex;
        }
        data.setVertexs(vertexs);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "vertexs.length=" + vertexs.length);
        }
    }
}
