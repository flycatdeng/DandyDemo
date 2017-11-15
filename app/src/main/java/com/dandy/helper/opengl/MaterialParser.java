package com.dandy.helper.opengl;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.StreamHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     ***.mat文件的解析帮助类；
 *     需要new关键字来创建对象以便调用；
 *     调用{@link #parse(InputStream)}来解析文件，从而得到该目录下的顶点着色器文件名和片元着色器名以及attribute和uniform字段；
 *     调用{@link #getFragmentFileName()}和{@link #getVertexFileName()}来获得对应的片元和顶点着色器文件名
 *     调用{@link #destroy()}来释放资源
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MaterialParser {
    private static final String TAG = "MaterialParser";
    private String vertexFileName = "";
    private String fragmentFileName = "";
    private ConcurrentHashMap<String, String> mHandlerNameAuthority = new ConcurrentHashMap<String, String>();

    public void parse(InputStream ins) {
        try {
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;
            // 扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {//一行
                // 用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");// [ ]+正则匹配多个连在一起的空格
                String typeFlag = tempsa[0].trim();
                if (typeFlag.equals("vertex_source_file")) {
                    vertexFileName = tempsa[1];//eg. vertex_source_file default_simple.vert
                } else if (typeFlag.equals("fragment_source_file")) {
                    fragmentFileName = tempsa[1];//eg. fragment_source_file default_simple.frag
                } else if (typeFlag.equals("attribute") || typeFlag.equals("uniform")) {
                    mHandlerNameAuthority.put(tempsa[2], typeFlag);//attribute/uniform
                }
            }
            StreamHelper.closeIOStream(br, isr);
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " load error e=" + e.getMessage());
            e.printStackTrace();
        } finally {
            StreamHelper.closeIOStream(ins);
        }
    }

    /**
     * 得到句柄对应的名称和权限集合，eg.position attribute
     * @return
     */
    public ConcurrentHashMap<String, String> getHandlerNameAuthority() {
        return mHandlerNameAuthority;
    }

    public String getVertexFileName() {
        return vertexFileName;
    }

    public String getFragmentFileName() {
        return fragmentFileName;
    }

    public void destroy() {
        if (mHandlerNameAuthority != null) {
            mHandlerNameAuthority.clear();
            mHandlerNameAuthority = null;
        }
    }
}
