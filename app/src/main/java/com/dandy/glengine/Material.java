package com.dandy.glengine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.java.basedata.StringHelper;
import com.dandy.helper.opengl.MaterialParser;
import com.dandy.helper.opengl.ShaderHelper;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     shader信息类
 *     通过new关键字来获取对象{@link #Material(Context, String)},该构造器暂时只支持从assets目录下获取.mat文件
 *     如果该类不在有用，请记得清楚数据{@link #destroy()}
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class Material {
    private static final String TAG = "Material";
    private String mMaterailName;
    private int mProgramID;
    private ConcurrentHashMap<String, Integer> mPropertyNameHandlerMap = new ConcurrentHashMap<String, Integer>();

    private Material() {
    }

    /**
     * 获得shader信息类
     * 该构造器暂时只支持从assets目录下获取.mat文件
     *
     * @param context            上下文
     * @param assetsMaterialFile .mat文件名
     */
    public static Material obtainFromAssets(Context context, String assetsMaterialFile) {
        Material result = new Material();
        String parentDir = StringHelper.getParentDirectory(assetsMaterialFile);//得到当前文件所在的文件夹
        MaterialParser parser = new MaterialParser();
        parser.parse(AssetsHelper.getInputStream(context, assetsMaterialFile));//解析.mat文件
        String vertexFile = parentDir + File.separator + parser.getVertexFileName();//得到.vert文件路劲
        String fragmentFile = parentDir + File.separator + parser.getFragmentFileName();//得到.frag文件路劲
        LogHelper.d(TAG, "vertexFile=" + vertexFile + " fragmentFile=" + fragmentFile);
        result.mProgramID = ShaderHelper.getProgram(
                AssetsHelper.getFileContent(context, vertexFile),//顶点着色器文本
                AssetsHelper.getFileContent(context, fragmentFile)//片元着色器文本
        );//获得shader的programID
        Map map = parser.getHandlerNameAuthority();//得到句柄对应的名称和权限集合，eg.position attribute
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            final String propertyName = (String) entry.getKey();
            final String propertyAuthority = (String) entry.getValue();
            int handler = -1;
            if (propertyAuthority.equals("attribute")) {
                handler = GLES20.glGetAttribLocation(result.mProgramID, propertyName);//通过mProgramID（shader）获得对应权限的名称及其返回的句柄ID
            } else if (propertyAuthority.equals("uniform")) {
                handler = GLES20.glGetUniformLocation(result.mProgramID, propertyName);
            }
            result.mPropertyNameHandlerMap.put(propertyName, handler);//每个属性名称和对应shader中的ID的集合
        }
        parser.destroy();//解析工作完成，释放资源
        return null;
    }

    /**
     * 通过属性名称获得其句柄id
     *
     * @param propertyName 属性名称 如：aPosition aNormal uMVPMatrix等
     * @return
     */
    public int getHandlerByPropertyName(String propertyName) {
        Integer result = mPropertyNameHandlerMap.get(propertyName);
        return result == null ? -1 : result;
    }

    /**
     * 得到当前program的ID
     *
     * @return
     */
    public int getProgramID() {
        return mProgramID;
    }

    public String getMaterailName() {
        return mMaterailName;
    }

    public void setmMaterailName(String materailSource) {
        this.mMaterailName = materailSource;
    }

    /**
     * 销毁，清楚数据
     */
    public void destroy() {
        if (mPropertyNameHandlerMap != null) {
            mPropertyNameHandlerMap.clear();
            mPropertyNameHandlerMap = null;
        }
    }
}
