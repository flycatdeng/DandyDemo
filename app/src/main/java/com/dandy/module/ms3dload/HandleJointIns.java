package com.dandy.module.ms3dload;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.io.SmallEndianInputStream;
import com.dandy.helper.java.math.Vec3;
import com.dandy.helper.opengl.Matrix4X4;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class HandleJointIns extends HandleInsChain {
    private static final String TAG = "HandleJointIns";

    @Override
    protected void onOwnLogicHandle(MS3DData data, SmallEndianInputStream fis) throws IOException {
        super.onOwnLogicHandle(data, fis);
        //加载关节信息
        int count = fis.readUnsignedShort();// 获取关节数量
        MS3DJoint[] joints = new MS3DJoint[count];// 创建关节信息对象数组
        // 创建关节信息对象与名称的map
        Map<String, MS3DJoint> map = new LinkedHashMap<String, MS3DJoint>();
        for (int i = 0; i < count; i++) {// 循环加载每个关节的信息，基本信息
            byte flagEdit = fis.readByte();// 标志 暂时无用，读了扔掉
            String name = fis.readString(32);// 读取关节名称
            String parentName = fis.readString(32);// 读取父关节名称，如果为空则表示没有父关节
            // 加载关节的初始旋转数据：3个4字节的float，分别代表欧拉角的三个分量
            Vec3 rotate = new Vec3(fis.readFloat(), fis.readFloat(), fis.readFloat());
            // 加载关节的初始位置数据：3个4字节的float，分别代表平移的XYZ值
            Vec3 position = new Vec3(fis.readFloat(), fis.readFloat(), fis.readFloat());
            // 读取关节旋转的关键帧数量
            int numKeyFramesRot = fis.readUnsignedShort();
            // 读取关节平移的关键帧数量
            int numKeyFramesPos = fis.readUnsignedShort();
            // 若关节旋转的关键帧数量不为0，则加载关节旋转的关键帧的值
            MS3DJointKeyFrameRotate[] keyFrameRotates = null;
            if (numKeyFramesRot > 0) {
                keyFrameRotates = HandleJointKeyFrameRotateIns.load(fis, numKeyFramesRot);
            }
            // 若关节平移的关键帧数量不为0，则加载关节平移的关键帧的值
            MS3DJointKeyFramePosition[] keyFramePositions = null;
            if (numKeyFramesPos > 0) {
                keyFramePositions = HandleJointKeyFramePositionIns.load(fis, numKeyFramesPos);
            }
            MS3DJoint ms3DJoint = new MS3DJoint(flagEdit, name, parentName, rotate, position, keyFrameRotates, keyFramePositions);
            joints[i] = ms3DJoint;
            map.put(ms3DJoint.getName(), ms3DJoint);// 将关节信息对象存储进map以备查找
        }//end of for
        //父关节等其他信息
        for (int i = 0; i < count; i++) {
            MS3DJoint joint = joints[i];
            joint.setParent(map.get(joint.getParentName()));// 获得此关节的父关节
            Matrix4X4 relative = new Matrix4X4();// 设置相对矩阵
            relative.loadIdentity();
            // 设置旋转
            relative.genRotationFromEulerAngle(joint.getBeginRotate());
            relative.setTranslation(joint.getBeginPosition()); // 设置平移
            Matrix4X4 absolute = new Matrix4X4();// 设置绝对矩阵
            absolute.loadIdentity();
            if (joint.getParent() != null) {// 是否有父关节
                // 有父关节的话绝对矩阵等于父关节的绝对矩阵乘以子关节的相对矩阵
                absolute.mul(joint.getParent().getAbsoluteMatrix(), relative);
            } else {
                // 无父关节的话相对矩阵即为绝对矩阵
                absolute.copyFrom(relative);
            }
            joint.setRelativeMatrix(relative);
            joint.setAbsoluteMatrix(absolute);
        }
        map.clear();// 清除map
        map = null;// 清除map
        data.setJoints(joints);
    }
}
