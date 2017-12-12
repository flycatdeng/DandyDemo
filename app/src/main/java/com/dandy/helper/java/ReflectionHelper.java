package com.dandy.helper.java;

import java.lang.reflect.Method;

public class ReflectionHelper {

    private static final String TAG = "ReflectionHelper";

    /**
     * 反射调用某静态方法得到所需要的值（Object需要强制转型）get the returned object by reflection without parameters
     * <p>
     * 需要注意的是这种方法是无参构造器，并且调用的是无参方法,静态方法
     * 
     * @param className
     *            要调用的类 the class which has a method named the methodName you give
     * @param methodName
     *            要掉用该类里的这个方法 the name of the method
     * @return
     * @throws Exception
     */
    public static Object getStaticMethodReturnObject(String className, String methodName) {
        return getStaticMethodReturnObject(className, methodName, null, null);
    }

    public static Object getStaticMethodReturnObject(String className, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object object = method.invoke(null, args);
            return object;
        } catch (Exception e) {
            com.dandy.helper.android.LogHelper.d(TAG, com.dandy.helper.android.LogHelper.getThreadName() + "Exception=" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射调用某方法得到所需要的值（Object需要强制转型）get the returned object by reflection without parameters
     * <p>
     * 需要注意的是这种方法是无参构造器，并且调用的是无参方法
     * 
     * @param className
     *            要调用的类 the class which has a method named the methodName you give
     * @param methodName
     *            要掉用该类里的这个方法 the name of the method
     * @return
     * @throws Exception
     */
    public static Object getMethodReturnObject(String className, String methodName) throws Exception {
        return getMethodReturnObject(className, methodName, null, null);
    }

    /**
     * 反射调用某方法得到所需要的值（Object需要强制转型）get the returned object by reflection without parameters
     * <p>
     * 这种方法是可以使用有参的构造器，但是调用的方法是无参的
     * 
     * @param className
     *            要调用的类 the class which has a method named the methodName you give,may be a abstract class like Context
     * @param instance
     *            要调用的类的实例，该实例可以是带有参数的实例，也可以是无参的实例 the instance of the giving className like context
     * @param methodName
     *            要掉用该类里的这个方法 the name of the method
     * @throws Exception
     */
    public static Object getMethodReturnObject(String className, Object instance, String methodName) throws Exception {
        return getMethodReturnObject(className, instance, methodName, null, null);
    }

    /**
     * 反射调用某方法得到所需要的值（Object需要强制转型），注意这种方式得到的构造器是无参的构造器
     * <p>
     * 如果该类找不到无参的构造器将会抛出异常
     * 
     * @param className
     *            要调用的类
     * @param methodName
     *            要掉用该类里的这个方法
     * @param parameterTypes
     *            方法对应的参数类型
     * @param args
     *            方法的参数实际的值
     * @return
     * @throws Exception
     */
    public static Object getMethodReturnObject(String className, String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
        Class<?> classes = Class.forName(className);
        Object instance = classes.newInstance();
        Method method = classes.getMethod(methodName, parameterTypes);
        Object roProductManufacturer = method.invoke(instance, args);
        return roProductManufacturer;
    }

    /**
     * 反射调用某方法得到所需要的值（Object需要强制转型）
     * <p>
     * 如果该类找不到无参的构造器将会抛出异常
     * 
     * @param className
     *            要调用的类
     * @param instance
     *            要调用的类的实例，该实例可以是带有参数的实例，也可以是无参的实例
     * @param methodName
     *            要掉用该类里的这个方法
     * @param parameterTypes
     *            方法对应的参数类型
     * @param args
     *            方法的参数实际的值
     * @return
     * @throws Exception
     */
    public static Object getMethodReturnObject(String className, Object instance, String methodName, Class<?>[] parameterTypes, Object[] args)
            throws Exception {
        Class<?> classes = Class.forName(className);
        if (instance == null) {
            throw new Exception("-----------反射获取类实例:" + className + "失败，返回");
        }
        Method method = classes.getMethod(methodName, parameterTypes);
        Object roProductManufacturer = method.invoke(instance, args);
        return roProductManufacturer;
    }

}
