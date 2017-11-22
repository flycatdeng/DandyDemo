package com.dandy.module.obj3dload;

import android.content.Context;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/11/22
 */
public class Obj3DBufferLoadAider {
    private static final String TAG = "Obj3DBufferLoadAider";
    private static final int LOAD_TASK_NUM = 3;//加载数量
    public static final int DATA_TYPE_VERTEX = 0;
    private static final int DATA_TYPE_NORMAL = 1;
    private static final int DATA_TYPE_TEXCOOR = 2;
    private ExecutorService mTaskThreadPool = Executors.newFixedThreadPool(LOAD_TASK_NUM);//3个线程
    private float[] vertexXYZ;//顶点坐标数组
    private float[] normalVectorXYZ;//法向量数组
    private float[] textureVertexST;//纹理坐标数组
    private long mStartParseTime = 0L;

    public void loadFromAssetsAsyn(final Context context, final String vertexFilePath, final String normalFilePath, final String texcoorFilePath, final OnLoadListener onLoadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStartParseTime = System.currentTimeMillis();
                loadFromInputStream(
                        AssetsHelper.getInputStream(context, vertexFilePath),
                        AssetsHelper.getInputStream(context, normalFilePath),
                        AssetsHelper.getInputStream(context, texcoorFilePath),
                        onLoadListener
                );
            }
        }).start();
    }

    public void loadFromPathAsyn(final String vertexFilePath, final String normalFilePath, final String texcoorFilePath, final OnLoadListener onLoadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStartParseTime = System.currentTimeMillis();
                try {
                    loadFromInputStream(
                            new FileInputStream(vertexFilePath),
                            new FileInputStream(normalFilePath),
                            new FileInputStream(texcoorFilePath),
                            onLoadListener
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    onLoadListener.onLoadFailed(e.getMessage());
                }
            }
        }).start();
    }

    public void loadFromInputStream(InputStream vertexIns, InputStream normalIns, InputStream texcoorIns, OnLoadListener onLoadListener) {
        if (mStartParseTime == 0L) {//如果是从path或者assets调用过来的，那就使用之前的解析时间
            mStartParseTime = System.currentTimeMillis();
        }
        ArrayList<LoadTask> loadTasks = new ArrayList<>();
        loadTasks.add(new LoadTask(vertexIns, DATA_TYPE_VERTEX, onLoadListener));
        loadTasks.add(new LoadTask(normalIns, DATA_TYPE_NORMAL, onLoadListener));
        loadTasks.add(new LoadTask(texcoorIns, DATA_TYPE_TEXCOOR, onLoadListener));
        try {
            mTaskThreadPool.invokeAll(loadTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogHelper.e(TAG, LogHelper.getThreadName(), e);
//            onLoadThreadFinished(0, e.getMessage(), onLoadListener);
            mTaskThreadPool.shutdownNow();
            onLoadListener.onLoadFailed(e.getMessage());
        }
    }

    public class LoadTask implements Callable<float[]> {
        private InputStream mIns;
        private int mDataType;
        private OnLoadListener mOnLoadListener;

        public LoadTask(InputStream ins, int dataType, OnLoadListener listener) {
            if (ins == null) {
                throw new RuntimeException("InputStream can not be null dataType=" + dataType);
            }
            mIns = ins;
            mDataType = dataType;
            mOnLoadListener = listener;
        }

        @Override
        public float[] call() throws Exception {
            byte[] data = new byte[mIns.available()];
            mIns.read(data, 0, data.length);
            ByteBuffer buff = ByteBuffer.wrap(data);
            buff.clear();
            float[] result = new float[buff.capacity() / 4];
            buff.asFloatBuffer().get(result);
            switch (mDataType) {
                case DATA_TYPE_VERTEX:
                    vertexXYZ = result;
                    break;
                case DATA_TYPE_NORMAL:
                    normalVectorXYZ = result;
                    break;
                case DATA_TYPE_TEXCOOR:
                    textureVertexST = result;
                    break;
            }
            onLoadThreadFinished(1, " load OK mDataType=" + mDataType, mOnLoadListener);
            return result;
        }
    }

    /**
     * 每次load task 执行完一次则加1
     */
    private int mLoadThreadFinishedCount = 0;
    /**
     * 加载成功了则加1
     */
    private int mLoadThreadSucceedCount = 0;
    private StringBuilder mLoadThreadMsg = new StringBuilder();

    private synchronized void onLoadThreadFinished(int flag, String msg, OnLoadListener onLoadListener) {
        mLoadThreadFinishedCount++;
        mLoadThreadSucceedCount += flag;
        mLoadThreadMsg.append(msg);
        if (mLoadThreadFinishedCount != LOAD_TASK_NUM) {
            return;
        }
        //所有的LoadTask执行完毕之后
        if (mLoadThreadSucceedCount == LOAD_TASK_NUM) {//每次加载都成功了
            long endParseTime = System.currentTimeMillis();
            //此处无法知道顶点等的个数
            Obj3DLoadResult result = new Obj3DLoadResult((endParseTime - mStartParseTime), -1, -1, -1, vertexXYZ.length / 3 / 3, vertexXYZ, normalVectorXYZ, textureVertexST);
            onLoadListener.onLoadOK(result);
        } else {//有没有加载成功的
            onLoadListener.onLoadFailed(mLoadThreadMsg.toString().trim());
        }
    }
}
