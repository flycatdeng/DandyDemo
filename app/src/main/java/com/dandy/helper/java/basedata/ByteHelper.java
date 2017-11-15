package com.dandy.helper.java.basedata;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class ByteHelper {
    private ByteHelper() {
    }

    public static int toUnsignedInt(byte var0) {
        return var0 & 255;
    }
}
