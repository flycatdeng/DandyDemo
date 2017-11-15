package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;
import com.dandy.helper.java.math.Vec3;

import java.io.IOException;

/**
 * <pre>
 *     加载三角形组装信息对象数组的方法
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleTriangleIns extends HandleInsChain {
    private static final String TAG = "HandleTriangleIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        int count = fis.readUnsignedShort();//读取三角形数量
        //创建三角形组装信息对象数组
        MS3DTriangle[] triangles = new MS3DTriangle[count];
        for (int i = 0; i < count; i++) {//循环加载每一个三角形的组装索引信息
            int flag = fis.readUnsignedShort();//标志-暂时无用，读了扔掉
            int[] indexs = new int[]{//加载索引
                    fis.readUnsignedShort(),
                    fis.readUnsignedShort(),
                    fis.readUnsignedShort()
            };
            Vec3[] normals = new Vec3[3];//加载三个顶点的法向量
            for (int j = 0; j < 3; j++) {
                normals[j] = new Vec3(
                        fis.readFloat(),
                        fis.readFloat(),
                        fis.readFloat()
                );
            }
            Vec3 s = new Vec3(//加载三个顶点的纹理S坐标
                    fis.readFloat(),
                    fis.readFloat(),
                    fis.readFloat()
            );
            Vec3 t = new Vec3(//加载三个顶点的纹理T坐标
                    fis.readFloat(),
                    fis.readFloat(),
                    fis.readFloat()
            );
            byte smoothingGroup = fis.readByte();//加载平滑组信息
            byte groupIndex = fis.readByte();//加载组信息
            MS3DTriangle triangle = new MS3DTriangle(flag, indexs, normals, s, t, smoothingGroup, groupIndex);
            triangles[i] = triangle;
        }
        data.setTriangles(triangles);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "triangles.length=" + triangles.length);
        }
    }
}
