package com.dandy.module.ms3dload;

import java.io.Serializable;

/**
 * <pre>
 *     MS3D文件头信息
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */
public class MS3DHeader implements Serializable {//
    private String id;
    private int version;

    public MS3DHeader(String id, int version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "MS3DHeader{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}
