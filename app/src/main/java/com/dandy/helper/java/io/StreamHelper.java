package com.dandy.helper.java.io;

import java.io.Closeable;

/**
 * <pre>
 *     IO流帮助类
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class StreamHelper {
    /**
     * 关闭io流
     *
     * @param closeable
     */
    public static void closeIOStream(Closeable... closeable) {
        if (closeable == null) {
            return;
        }
        for (Closeable ca : closeable) {
            try {
                if (ca == null) {
                    continue;
                }
                ca.close();
                ca = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
