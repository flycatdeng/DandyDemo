package com.dandy.helper.android.res;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.StreamHelper;

import java.io.IOException;
import java.io.InputStream;


/**
 * <pre>
 *     A class with some help methods of Assets
 *     Assets的一些帮助方法类
 * </pre>
 * Created by Dandy
 * Wechat: flycatdeng
 */
public class AssetsHelper {
    private static final String TAG = "AssetsHelper";

    /**
     * 从assets目录下获取文本文件内容
     *
     * @param context       上下文
     * @param fileAssetPath 文本文件路径
     * @return
     */
    public static String getFileContent(Context context, String fileAssetPath) {
        InputStream ins = null;
        try {
            ins = context.getAssets().open(fileAssetPath);
            byte[] contentByte = new byte[ins.available()];
            ins.read(contentByte);
            return new String(contentByte);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            StreamHelper.closeIOStream(ins);
        }
        return "";
    }

    /**
     * <pre>
     * 从Assets获取图片
     * </pre>
     *
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        return BitmapFactory.decodeStream(getInputStream(context, fileName));
    }

    /**
     * 得到文件流
     *
     * @param context
     * @param fileAssetPath 文件名称
     * @return
     */
    public static InputStream getInputStream(Context context, String fileAssetPath) {
        InputStream ins = null;
        try {
            ins = context.getAssets().open(fileAssetPath);
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.d(TAG, LogHelper.getThreadName() + " e=" + e.getMessage());
        }
        return ins;
    }

    /**
     * <pre>
     * 得到assets目录下的文件的绝对路劲
     * </pre>
     *
     * @param assetsName
     * @return
     */
    public static String getFileAbsolutePath(String assetsName) {
        return "file:///android_asset/" + assetsName;
    }

}
