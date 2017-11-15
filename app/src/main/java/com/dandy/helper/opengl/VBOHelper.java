package com.dandy.helper.opengl;

import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;

import java.nio.Buffer;
import java.util.List;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class VBOHelper {
    private static final String TAG = "VBOHelper";

    /**
     * @param vboOptionsList
     * @param releaseBind    是否主动调用不使用这个缓冲区了，如果是2.0需要主动，如果是3.0且用到了VAO的话则可以不用主动释放
     */
    public static void initVBOs(List<VBOOptions> vboOptionsList, boolean releaseBind) {
        if (vboOptionsList == null || vboOptionsList.size() == 0) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " vboOptionsList == null || vboOptionsList.size() == 0");
            return;
        }
        int size = vboOptionsList.size();
        int[] vboIDs = new int[size];
        GLES20.glGenBuffers(size, vboIDs, 0);//申请一个缓冲区
        for (int i = 0; i < size; i++) {
            VBOOptions vboOptions = vboOptionsList.get(i);
            GLES20.glBindBuffer(vboOptions.target, vboIDs[i]);//绑定缓冲区
            GLES20.glBufferData(vboOptions.target, vboOptions.size, vboOptions.buffer, vboOptions.usage);
            vboOptions.vboId = vboIDs[i];
        }
        if (releaseBind) {
            //TODO 目前只有这两种，如果还有其他的种类的话再重构吧
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);//现在不使用这个缓冲区
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);//现在不使用这个缓冲区
        }
    }

    public static void drawVBOs(List<VBOOptions> vboOptionsList) {
        if (vboOptionsList == null || vboOptionsList.size() == 0) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " vboOptionsList == null || vboOptionsList.size() == 0");
            return;
        }
        int size = vboOptionsList.size();
        for (int i = 0; i < size; i++) {
            drawOneVBO(vboOptionsList.get(i));
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);//数据已经得到 就可以不再使用这个绑定了
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);//现在不使用这个缓冲区
    }

    public static void drawOneVBO(VBOOptions op) {
        int vboId = op.getVboId();
        int glVertexAttribHandleID = op.getGlVertexAttribHandleID();
        if (vboId == -1 || glVertexAttribHandleID == -1) {
            LogHelper.d(TAG, op.getTag() + " op.getVboId() == -1 || op.getGlVertexAttribHandleID() == -1");
            return;
        }
        GLES20.glBindBuffer(op.getTarget(), vboId);//绑定存在的VBO
        GLES20.glVertexAttribPointer(glVertexAttribHandleID, //要配置的顶点属性句柄
                op.getGlVertexAttribSize(),//指定顶点属性的大小。顶点属性是一个vec3，它由3个值组成，所以大小是3
                op.getGlVertexAttribType(),//类似GLES20.GL_FLOAT
                op.isGlVertexAttribNormalized(),//是否规格化
                //步长(Stride)，它告诉我们在连续的顶点属性组之间的间隔。由于下个组位置数据在3个GLfloat之后，我们把步长设置为3 * sizeof(GLfloat)。
                // 要注意的是由于我们知道这个数组是紧密排列的（在两个顶点属性之间没有空隙）我们也可以设置为0来让OpenGL决定具体步长是多少（只有当数值是紧密排列时才可用）。
                // 一旦我们有更多的顶点属性，我们就必须更小心地定义每个顶点属性之间的间隔
                op.getGlVertexAttribStride(),
                op.getGlVertexAttribOffset());//顶点XYZ,三个点，使用GPU中的缓冲数据，不再从RAM中取数据，所以后面的2个参数都是0
        GLES20.glEnableVertexAttribArray(glVertexAttribHandleID);//开启顶点
    }

    public static class VBOOptions {
        /**
         * TAG
         */
        private String tag;
        /**
         * <pre>
         *     GL_ARRAY_BUFFER
         *     GL_ELEMENT_ARRAY_BUFFER之类的
         * </pre>
         */
        private int target;
        /**
         * 这个是需要在应用之后被赋值的，而不是创建的时候
         */
        private int vboId = -1;
        /**
         * <pre>
         *     字节数
         *     例如：｛2，1，2｝为3*4 3个元素*每个int为4字节
         * </pre>
         */
        private int size;
        /**
         * 数据Buffer，一般都是转换成了FloatBuffer,ShortBuffer之类的
         */
        private Buffer buffer;
        /**
         * <pre>
         *     GL_STATIC_DRAW ：数据不会或几乎不会改变。
         *     GL_DYNAMIC_DRAW：数据会被改变很多。
         *     GL_STREAM_DRAW ：数据每次绘制时都会改变。
         * </pre>
         */
        private int usage;
        /**
         * 在program中对应的ID
         */
        private int glVertexAttribHandleID = -1;
        /**
         * 一个数据对应几个元素？ 例如（x,y,z）就对应为3
         */
        private int glVertexAttribSize;
        /**
         * 数据类型，如GL_FLOAT
         */
        private int glVertexAttribType;
        /**
         * 传输的数据是否规格化了
         */
        private boolean glVertexAttribNormalized;
        /**
         * 传输的数据步长
         */
        private int glVertexAttribStride;
        /**
         * 传输的数据Offset
         */
        private int glVertexAttribOffset;

        private VBOOptions() {
        }

        private VBOOptions(String tag, int target, int size, Buffer buffer, int usage,
                           int glVertexAttribHandleID, int glVertexAttribSize, int glVertexAttribType, boolean glVertexAttribNormalized, int glVertexAttribStride, int glVertexAttribOffset) {
            this.tag = tag;
            this.target = target;
            this.size = size;
            this.buffer = buffer;
            this.usage = usage;
            this.glVertexAttribHandleID = glVertexAttribHandleID;
            this.glVertexAttribSize = glVertexAttribSize;
            this.glVertexAttribType = glVertexAttribType;
            this.glVertexAttribNormalized = glVertexAttribNormalized;
            this.glVertexAttribStride = glVertexAttribStride;
            this.glVertexAttribOffset = glVertexAttribOffset;
        }

        private VBOOptions(String tag, int target, int size, Buffer buffer, int usage) {
            this.tag = tag;
            this.target = target;
            this.size = size;
            this.buffer = buffer;
            this.usage = usage;
        }

        public static VBOOptions obtainDrawOptions(String tag, int target, int size, Buffer buffer, int usage,
                                                   int glVertexAttribHandleID, int glVertexAttribSize, int glVertexAttribType, boolean glVertexAttribNormalized, int glVertexAttribStride, int glVertexAttribOffset) {
            VBOOptions vboOptions = new VBOOptions(tag, target, size, buffer, usage);
            vboOptions.setDrawOptions(glVertexAttribHandleID, glVertexAttribSize, glVertexAttribType, glVertexAttribNormalized, glVertexAttribStride, glVertexAttribOffset);
            return vboOptions;
        }

        public static VBOOptions obtainBaseOptions(String tag, int target, int size, Buffer buffer, int usage) {
            return new VBOOptions(tag, target, size, buffer, usage);
        }

        public static VBOOptions obtainEnptyOptions() {
            return new VBOOptions();
        }

        public void setDrawOptions(int glVertexAttribHandleID, int glVertexAttribSize, int glVertexAttribType, boolean glVertexAttribNormalized, int glVertexAttribStride, int glVertexAttribOffset) {
            this.glVertexAttribHandleID = glVertexAttribHandleID;
            this.glVertexAttribSize = glVertexAttribSize;
            this.glVertexAttribType = glVertexAttribType;
            this.glVertexAttribNormalized = glVertexAttribNormalized;
            this.glVertexAttribStride = glVertexAttribStride;
            this.glVertexAttribOffset = glVertexAttribOffset;
        }

        @Override
        public String toString() {
            return "VBOOptions{" +
                    "tag='" + tag + '\'' +
                    ", target=" + target +
                    ", vboId=" + vboId +
                    ", size=" + size +
                    ", buffer=" + buffer +
                    ", usage=" + usage +
                    ", glVertexAttribHandleID=" + glVertexAttribHandleID +
                    ", glVertexAttribSize=" + glVertexAttribSize +
                    ", glVertexAttribType=" + glVertexAttribType +
                    ", glVertexAttribNormalized=" + glVertexAttribNormalized +
                    ", glVertexAttribStride=" + glVertexAttribStride +
                    ", glVertexAttribOffset=" + glVertexAttribOffset +
                    '}';
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getVboId() {
            return vboId;
        }

        public void setVboId(int vboId) {
            this.vboId = vboId;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Buffer getBuffer() {
            return buffer;
        }

        public void setBuffer(Buffer buffer) {
            this.buffer = buffer;
        }

        public int getUsage() {
            return usage;
        }

        public void setUsage(int usage) {
            this.usage = usage;
        }

        public int getGlVertexAttribHandleID() {
            return glVertexAttribHandleID;
        }

        public void setGlVertexAttribHandleID(int glVertexAttribHandleID) {
            this.glVertexAttribHandleID = glVertexAttribHandleID;
        }

        public int getGlVertexAttribSize() {
            return glVertexAttribSize;
        }

        public void setGlVertexAttribSize(int glVertexAttribSize) {
            this.glVertexAttribSize = glVertexAttribSize;
        }

        public int getGlVertexAttribType() {
            return glVertexAttribType;
        }

        public void setGlVertexAttribType(int glVertexAttribType) {
            this.glVertexAttribType = glVertexAttribType;
        }

        public boolean isGlVertexAttribNormalized() {
            return glVertexAttribNormalized;
        }

        public void setGlVertexAttribNormalized(boolean glVertexAttribNormalized) {
            this.glVertexAttribNormalized = glVertexAttribNormalized;
        }

        public int getGlVertexAttribStride() {
            return glVertexAttribStride;
        }

        public void setGlVertexAttribStride(int glVertexAttribStride) {
            this.glVertexAttribStride = glVertexAttribStride;
        }

        public int getGlVertexAttribOffset() {
            return glVertexAttribOffset;
        }

        public void setGlVertexAttribOffset(int glVertexAttribOffset) {
            this.glVertexAttribOffset = glVertexAttribOffset;
        }
    }

}
