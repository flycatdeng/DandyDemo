package com.dandy.module.ms3dload;

import java.io.Serializable;

/**
 * <pre>
 *
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DData implements Serializable {
    private MS3DHeader header;
    private MS3DVertex[] vertexs;
    private long loadTime;
    private MS3DTriangle[] triangles;
    private MS3DGroup[] groups;
    private MS3DFrameTime frameTime;
    private MS3DMaterial[] materials;
    private MS3DJoint[] joints;

    public void setHeader(MS3DHeader header) {
        this.header = header;
    }

    public void setVertexs(MS3DVertex[] vertexs) {
        this.vertexs = vertexs;
    }

    public void setTriangles(MS3DTriangle[] triangles) {
        this.triangles = triangles;
    }

    public void setGroups(MS3DGroup[] groups) {
        this.groups = groups;
    }

    public void setFrameTime(MS3DFrameTime frameTime) {
        this.frameTime = frameTime;
    }

    public void setMaterials(MS3DMaterial[] materials) {
        this.materials = materials;
    }

    public void setJoints(MS3DJoint[] joints) {
        this.joints = joints;
    }

    public MS3DHeader getHeader() {
        return header;
    }

    public MS3DVertex[] getVertexs() {
        return vertexs;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public MS3DTriangle[] getTriangles() {
        return triangles;
    }

    public MS3DGroup[] getGroups() {
        return groups;
    }

    public MS3DFrameTime getFrameTime() {
        return frameTime;
    }

    public MS3DMaterial[] getMaterials() {
        return materials;
    }

    public MS3DJoint[] getJoints() {
        return joints;
    }

    private long mStartParseTime = 0L;

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    protected void startCountParseTime() {
        mStartParseTime = System.currentTimeMillis();
    }

    protected void stopCountParseTime() {
        setLoadTime(System.currentTimeMillis() - mStartParseTime);
    }

    @Override
    public String toString() {
        return "MS3DData{" +
                "header=" + header.toString() +
                ", vertexs.length=" + vertexs.length +
                ", loadTime=" + loadTime +
                ", triangles.length=" + triangles.length +
                ", groups.length=" + groups.length +
                ", frameTime=" + frameTime.toString() +
                ", materials.length=" + materials.length +
                ", joints.length=" + joints.length +
                '}';
    }
}
