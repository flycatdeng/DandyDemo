package com.dandy.demo.tab.opengl;

import android.content.Context;

import com.dandy.glengine.SimpleImage;
import com.dandy.glengine.Stage;
import com.dandy.glengine.android.ISimpleGLContent;
import com.dandy.glengine.android.SimpleGLActivity;
import com.dandy.glengine.android.SimpleGLContentAdapter;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class SimpleEngineImageAty extends SimpleGLActivity {
    @Override
    public ISimpleGLContent getSimpleGLContent(final Context context) {
        return new SimpleGLContentAdapter() {
            private SimpleImage mImage;

            @Override
            public void onCreate(Stage stage) {
                mImage = SimpleImage.createFromAssets(context, "demo/test_image.jpg");
                stage.add(mImage);
            }
        };
    }
}
