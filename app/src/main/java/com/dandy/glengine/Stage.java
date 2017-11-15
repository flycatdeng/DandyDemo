package com.dandy.glengine;

import android.content.Context;
import android.opengl.GLES20;

public class Stage extends Group {
    public Stage(Context context) {
        super(context);
    }

    @Override
    protected void onChildAdded(final Actor child) {
//        child.requestRender();
        child.setRequestRenderListener(getRequestRenderListener());
        if (mIsResumed) {
            child.onResume();
        }
        if (child.isSurfaceCreated()) {
            return;
        }
        child.addRunOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {//不用担心没有值，因为这个是在onDraw的时候才会调用的
                child.onSurfaceCreated();
                child.onSurfaceChanged(mSurfaceWidth, mSurfaceHeight);
            }
        });
        child.requestRender();
    }

    //    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
//        for (final Actor child : mChildren) {
//            child.onDrawFrame();
//        }
        for (int i = 0; i < mChildren.size(); i++) {
            mChildren.get(i).onDrawFrame();
        }
    }

    /**
     * Add the actors into this container.
     *
     * @param actors actors to add as children
     */
    public void add(Actor... actors) {
        addChild(actors);
    }

    @Override
    public void onResume() {
        super.onResume();
        for (final Actor child : mChildren) {
            child.onResume();
        }
    }

    @Override
    public void onPause() {
        for (final Actor child : mChildren) {
            child.onPause();
        }
    }

    @Override
    public void onDestroy() {
        for (final Actor child : mChildren) {
            child.onDestroy();
        }
        mChildren.clear();
    }
}
