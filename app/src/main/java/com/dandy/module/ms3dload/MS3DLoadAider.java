package com.dandy.module.ms3dload;

import android.content.Context;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;
import com.dandy.helper.java.io.StreamHelper;

import java.io.InputStream;

/**
 * <pre>
 *
 * </pre>
 * Created by Dandy
 * Wechat: flycatdeng
 */
public class MS3DLoadAider {
    private long mStartLoadTime = -1L;

//    public void loadFromAssetsAsync(final Context context, final String filePath, final IOnLoadCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mStartLoadTime = System.currentTimeMillis();
//                loadFromInputStreamSync(AssetsHelper.getInputStream(context, filePath), callback);
//            }
//        }).start();
//    }

    public void loadFromInputStreamAsync(final InputStream ins, final IOnLoadCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStartLoadTime = System.currentTimeMillis();
                loadFromInputStreamSync(ins, callback);
            }
        }).start();
    }

    private void loadFromInputStreamSync(InputStream ins, IOnLoadCallback callback) {
        MS3DData data = new MS3DData();
        data.startCountParseTime();
        try {
            SmallEndianInputStream fis = new SmallEndianInputStream(ins);//将输入流封装为SmallEndian格式的输入流
            IHandleInsChain headHandler = new HandleHeadIns();//处理文件头信息
            IHandleInsChain vertexHandler = new HandleVertexIns();//处理顶点信息
            IHandleInsChain triangleHandler = new HandleTriangleIns();//处理三角形组装索引信息
            IHandleInsChain groupHandler = new HandleGroupIns();//处理组信息
            IHandleInsChain materialHandler = new HandleMaterialIns();//处理材质信息
            IHandleInsChain frameTimeHandler = new HandleFrameTimeIns();//处理帧速率信息、当前时间、关键帧数、动画总时间
            IHandleInsChain jointHandler = new HandleJointIns();//处理关节信息
            headHandler.setNextHandler(vertexHandler);
            vertexHandler.setNextHandler(triangleHandler);
            triangleHandler.setNextHandler(groupHandler);
            groupHandler.setNextHandler(materialHandler);
            materialHandler.setNextHandler(frameTimeHandler);
            frameTimeHandler.setNextHandler(jointHandler);
            jointHandler.setNextHandler(null);//null 表示接下来没有要处理的了
            headHandler.handleIns(fis, data, callback);
        } finally {
            StreamHelper.closeIOStream(ins);
        }
    }
}
