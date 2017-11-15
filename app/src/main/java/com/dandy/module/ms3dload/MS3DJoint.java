package com.dandy.module.ms3dload;

import com.dandy.helper.java.math.Vec3;
import com.dandy.helper.opengl.Matrix4X4;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DJoint implements Serializable {

    private byte flagEdit;//关节在编辑器中的状态
    private String name; //关节名称
    private String parentName;//父关节名称，若为空则表示此关节无父关节
    private Vec3 beginRotate;//初始旋转值
    private Vec3 beginPosition;//初始平移值
    private MS3DJointKeyFrameRotate[] keyFrameRotates;//旋转关键帧数据
    private MS3DJointKeyFramePosition[] keyFramePositions;//平移关键数据

    public MS3DJoint(byte flagEdit, String name, String parentName, Vec3 beginRotate, Vec3 beginPosition, MS3DJointKeyFrameRotate[] keyFrameRotates, MS3DJointKeyFramePosition[] keyFramePositions) {
        this.flagEdit = flagEdit;
        this.name = name;
        this.parentName = parentName;
        this.beginRotate = beginRotate;
        this.beginPosition = beginPosition;
        this.keyFrameRotates = keyFrameRotates;
        this.keyFramePositions = keyFramePositions;
    }

    private MS3DJoint parent;
    private Matrix4X4 relativeMatrix;
    private Matrix4X4 absoluteMatrix;

    public Matrix4X4 getRelativeMatrix() {
        return relativeMatrix;
    }

    public void setRelativeMatrix(Matrix4X4 relativeMatrix) {
        this.relativeMatrix = relativeMatrix;
    }

    public Matrix4X4 getAbsoluteMatrix() {
        return absoluteMatrix;
    }

    public void setAbsoluteMatrix(Matrix4X4 absoluteMatrix) {
        this.absoluteMatrix = absoluteMatrix;
    }

    public MS3DJoint getParent() {
        return parent;
    }

    public void setParent(MS3DJoint parent) {
        this.parent = parent;
    }

    public byte getFlagEdit() {
        return flagEdit;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public Vec3 getBeginRotate() {
        return beginRotate;
    }

    public Vec3 getBeginPosition() {
        return beginPosition;
    }

    public MS3DJointKeyFrameRotate[] getKeyFrameRotates() {
        return keyFrameRotates;
    }

    public MS3DJointKeyFramePosition[] getKeyFramePositions() {
        return keyFramePositions;
    }
}
