package com.dandy.demo.tab.opengl;

import android.content.Context;

import com.dandy.glengine.ElementTexture;
import com.dandy.glengine.Stage;
import com.dandy.glengine.android.ISimpleGLContent;
import com.dandy.glengine.android.SimpleGLActivity;
import com.dandy.glengine.android.SimpleGLContentAdapter;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.opengl.TextureHelper;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */
public class ElementTextureAty extends SimpleGLActivity {

    @Override
    public ISimpleGLContent getSimpleGLContent(final Context context) {
        return new SimpleGLContentAdapter() {
            private ElementTexture mActor;

            @Override
            public void onCreate(Stage stage) {
                super.onCreate(stage);
                mActor = new ElementTexture(context);
                mActor.setTexture(AssetsHelper.getBitmap(context,"demo/test_image.jpg"));
                stage.add(mActor);
            }
        };
    }
}
