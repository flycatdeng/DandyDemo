package com.dandy.helper.java.basedata;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class ShortHelper {
    private ShortHelper() {
    }

    public static int toUnsignedInt(short var0) {
        return var0 & '\uffff';
    }
}
