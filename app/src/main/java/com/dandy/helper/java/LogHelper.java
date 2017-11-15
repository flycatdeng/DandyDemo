package com.dandy.helper.java;

/**
 * 打印帮助类
 * 
 * @author dengchukun
 * 
 */
public class LogHelper {
	private static final String ROOT_TAG = "dandy";
	private static boolean sIsLogDebug = true;
	private static String sRootTag = ROOT_TAG;

	/**
	 * 打印log详细信息 相见LogDemo类和MainActivity类 最好是每个方法中都调用此方法
	 */
	public static void d(String tag, String content) {
		if (sIsLogDebug) {
			System.out.println(sRootTag + "_" + tag + ":" + content);
		}
	}

	// class DetailLogDemo
	/**
	 * 打印一段字符串
	 * 
	 * @param content
	 */
	public static void printLog(String content) {
		if (sIsLogDebug) {
			System.out.println(sRootTag + ":" + content);
		}
	}

	/**
	 * 得到调用此方法的线程的线程名
	 * 
	 * @return
	 */
	public static String getThreadName() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(Thread.currentThread().getName());
			sb.append("-> ");
			sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
			sb.append("()");
			sb.append(" ");
		} catch (Exception e) {
		}
		return sb.toString();
	}

	public static boolean isLogDebug() {
		return sIsLogDebug;
	}

	public static void setLogDebug(boolean isLogDebug) {
		sIsLogDebug = isLogDebug;
	}

	public static String getRootTag() {
		return sRootTag;
	}

	public static void setRootTag(String rootTag) {
		sRootTag = rootTag;
	}
}
