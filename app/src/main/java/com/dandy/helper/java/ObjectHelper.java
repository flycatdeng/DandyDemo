package com.dandy.helper.java;

import java.util.Locale;

public class ObjectHelper {
    /**
     * 判断对象是否非空
     * 
     * @param object
     * @return true:空 false:非空
     */
    public static boolean isNull(Object object) {
        if (object == null || "".equals(object.toString().trim()) || "null".equals(object.toString().trim().toLowerCase(Locale.getDefault()))) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * 获取类名
     * 
     * @return 当前class的名称
     */
    public static String getClassName(Class<?> classes) {
        if (!isNull(classes)) {
            return classes.getName();
        }
        return "";
    }

    /**
     * 获取类名
     * 
     * @return 当前class的名称
     */
    public static String getClassSimpleName(Class<?> classes) {
        if (!isNull(classes)) {
            return classes.getSimpleName();
        }
        return "";
    }

    public static Class<?> getClassByFullName(String classFullName) {
        try {
            Class<?> clazz = Class.forName(classFullName);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
