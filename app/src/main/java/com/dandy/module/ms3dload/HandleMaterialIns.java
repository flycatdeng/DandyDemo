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

public class HandleMaterialIns extends HandleInsChain {
    private static final String TAG = "HandleMaterialIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        //加载材质信息的方法
        int count = fis.readUnsignedShort();//读取材质数量
        //创建材质信息对象数组
        MS3DMaterial[] mals = new MS3DMaterial[count];
        for (int i = 0; i < count; i++) {//循环加载每个材质的信息
            String name = fis.readString(32);//读取材质的名称
            float[] ambient_color = new float[4];//读取环境光信息
            for (int j = 0; j < 4; j++) {
                ambient_color[j] = fis.readFloat();
            }
            float[] diffuse_color = new float[4];//读取散射光信息
            for (int j = 0; j < 4; j++) {
                diffuse_color[j] = fis.readFloat();
            }
            float[] specular_color = new float[4];//读取镜面光信息
            for (int j = 0; j < 4; j++) {
                specular_color[j] = fis.readFloat();
            }
            float[] emissive_color = new float[4];//读取自发光信息
            for (int j = 0; j < 4; j++) {
                emissive_color[j] = fis.readFloat();
            }
            float shininess = fis.readFloat();//读取粗糙度信息
            float transparency = fis.readFloat();//读取透明度信息
            byte flagMode = fis.readByte();//mode 暂时无用，读了扔掉
            //读取纹理图片名称
            String textureName = format(fis.readString(128));
            String transparencyTextureName = fis.readString(128);//透明材质 暂时无用，读了扔掉
            MS3DMaterial mal = new MS3DMaterial(name, ambient_color, diffuse_color, specular_color, emissive_color,
                    shininess, transparency, flagMode, textureName, transparencyTextureName);
            mals[i] = mal;
            //添加纹理（也就是加载纹理图）
        }
        data.setMaterials(mals);
        if (LogHelper.isLogDebug()) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " mals.length=" + mals.length);
        }
    }

    //从文件路径中摘取出纹理图的文件名，如“xx.jpg”
    private final static String format(String path) {
        int offset = path.lastIndexOf("\\");
        if (offset > -1) {
            return path.substring(offset + 1);
        }
        return path;
    }
}
