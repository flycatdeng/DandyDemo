package com.dandy.demo.tab.opengl;

import android.content.Context;

import com.dandy.glengine.Stage;
import com.dandy.glengine.android.ISimpleGLContent;
import com.dandy.glengine.android.SimpleGLActivity;
import com.dandy.glengine.android.SimpleGLContentAdapter;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.module.obj3dload.ActorObject3D;
import com.dandy.module.obj3dload.Obj3DBufferLoadAider;
import com.dandy.module.obj3dload.Obj3DLoadResult;
import com.dandy.module.obj3dload.OnLoadListener;

/**
 * <pre>
 *     使用Obj3DBufferLoadAider帮助类，快速加载obj数据方式的demo
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/11/22
 */
public class Obj3DBufferLoadAiderAty extends SimpleGLActivity {
    private static final String TAG = "Obj3DBufferLoadAiderAty";
    private static final String ROOT_DIR = "sdcard/dandy";

    @Override
    public ISimpleGLContent getSimpleGLContent(final Context context) {
        return new SimpleGLContentAdapter() {
            ActorObject3D obj;

            @Override
            public void onCreate(final Stage stage) {
                super.onCreate(stage);
                obj = new ActorObject3D(context);
                obj.setTexture(AssetsHelper.getBitmap(context, "demo/obj/head.jpg"));
//                new Obj3DLoadAider().loadFromInputStreamAsync(AssetsHelper.getInputStream(context, "demo/obj/head.obj"), new OnLoadListener() {
//                    @Override
//                    public void onLoadOK(Obj3DLoadResult result) {
//                        LogHelper.d(TAG, LogHelper.getThreadName() + " Obj3DLoadResult result=" + result.toString());
//                        try {
//                            File dirFile = new File(ROOT_DIR);
//                            if (!dirFile.exists()) {
//                                dirFile.mkdirs();
//                            }
//                            JBufferCacheHelper.writeCache(result.getVertexXYZ(), ROOT_DIR + "/" + "head.vxyz");
//                            JBufferCacheHelper.writeCache(result.getNormalVectorXYZ(), ROOT_DIR + "/" + "head.nxyz");
//                            JBufferCacheHelper.writeCache(result.getTextureVertexST(), ROOT_DIR + "/" + "head.tst");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFailed(String failedMsg) {
//                        LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
//                    }
//                });
                //使用上述方式加载需要678ms，而使用下述方式只需要3ms
                new Obj3DBufferLoadAider().loadFromPathAsyn(//如果一下三个文件不存在的话，建议使用上述注释的代码生成对应的文件
                        ROOT_DIR + "/" + "head.vxyz",
                        ROOT_DIR + "/" + "head.vxyz",
                        ROOT_DIR + "/" + "head.vxyz",
                        new OnLoadListener() {
                            @Override
                            public void onLoadOK(Obj3DLoadResult result) {
                                LogHelper.d(TAG, LogHelper.getThreadName() + " Obj3DLoadResult result=" + result.toString());
                                obj.loadFromData(result.getVertexXYZ(), result.getNormalVectorXYZ(), result.getTextureVertexST());
                                stage.add(obj);
                                obj.scale(0.1f, 0.1f, 0.1f);
                                obj.rotate(30f, 0f, 1f, 0f);
                            }

                            @Override
                            public void onLoadFailed(String failedMsg) {
                                LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
                            }
                        }
                );
            }
        };
    }
}
