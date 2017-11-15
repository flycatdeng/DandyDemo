package com.dandy.demo.tab.opengl;

import android.content.Context;

import com.dandy.glengine.Stage;
import com.dandy.glengine.android.ISimpleGLContent;
import com.dandy.glengine.android.SimpleGLActivity;
import com.dandy.glengine.android.SimpleGLContentAdapter;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.module.obj3dload.ActorObject3D;
import com.dandy.module.obj3dload.Obj3DLoadAider;
import com.dandy.module.obj3dload.Obj3DLoadResult;
import com.dandy.module.obj3dload.OnLoadListener;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class Obj3DLoadAiderAty extends SimpleGLActivity {
    private static final String TAG = "Obj3DLoadAiderAty";

    @Override
    public ISimpleGLContent getSimpleGLContent(final Context context) {
        return new SimpleGLContentAdapter() {
            ActorObject3D obj;

            @Override
            public void onCreate(final Stage stage) {
                super.onCreate(stage);
                obj = new ActorObject3D(context);
                obj.setTexture(AssetsHelper.getBitmap(context, "demo/obj/head.jpg"));
                new Obj3DLoadAider().loadFromInputStreamAsync(AssetsHelper.getInputStream(context, "demo/obj/changgui.obj"), new OnLoadListener() {
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
                });
            }
        };
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LogHelper.d(TAG, LogHelper.getThreadName());
//        new Obj3DLoadAider().loadFromInputStreamAsync(AssetsHelper.getInputStream(this, "demo/obj/head.obj"), new OnLoadListener() {
//            @Override
//            public void onLoadOK(Obj3DLoadResult result) {
//                LogHelper.d(TAG, LogHelper.getThreadName() + " Obj3DLoadResult result=" + result.toString());
//            }
//
//            @Override
//            public void onLoadFailed(String failedMsg) {
//                LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
//            }
//        });
//    }
}
