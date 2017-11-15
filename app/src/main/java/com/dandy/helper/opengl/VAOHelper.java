package com.dandy.helper.opengl;

import android.opengl.GLES30;

import com.dandy.helper.android.LogHelper;

import java.util.List;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class VAOHelper {
    private static final String TAG = "VAOHelper";

    /**
     * @param vaoIDs         目前只支持length为1的数组,如果是null则会自动创建一个数组
     * @param vboOptionsList 依赖VBOOptions
     */
    public static void initVAOs(int[] vaoIDs, List<VBOHelper.VBOOptions> vboOptionsList) {
        if (vaoIDs == null) {
            vaoIDs = new int[1];
        }
        if (vaoIDs.length != 1) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " vaoIDs.length!=1");
            return;
        }
        GLES30.glGenVertexArrays(vaoIDs.length, vaoIDs, 0);
        GLES30.glBindVertexArray(vaoIDs[0]);
        int size = vboOptionsList.size();
        for (int i = 0; i < size; i++) {
            VBOHelper.VBOOptions vboOptions = vboOptionsList.get(i);
            GLES30.glBindBuffer(vboOptions.getTarget(), vboOptions.getVboId());
            GLES30.glEnableVertexAttribArray(vboOptions.getGlVertexAttribHandleID());
            GLES30.glVertexAttribPointer(vboOptions.getGlVertexAttribHandleID(), vboOptions.getGlVertexAttribSize(),
                    vboOptions.getGlVertexAttribType(), vboOptions.isGlVertexAttribNormalized(), vboOptions.getGlVertexAttribStride(),
                    vboOptions.getGlVertexAttribOffset());
        }
        GLES30.glBindVertexArray(0);
    }
}
