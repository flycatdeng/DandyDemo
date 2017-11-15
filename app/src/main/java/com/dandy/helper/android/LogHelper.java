package com.dandy.helper.android;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * <pre>
 *     log help class
 *     打印帮助类
 * </pre>
 * Created by Dandy
 * Wechat: flycatdeng
 */
public class LogHelper {
    private static final String ROOT_TAG = "dandydeng";
    private static boolean sIsLogDebug = true;
    private static String sRootTag = ROOT_TAG;
    private static boolean sIsToastDebug = true;

    /**
     * 打印log详细信息 相见LogDemo类和MainActivity类 最好是每个方法中都调用此方法
     */
    public static void d(String tag, String content) {
        if (sIsLogDebug) {
            Log.d(sRootTag + "_" + tag, content);
        }
    }

    public static void e(String tag, String content) {
        Log.d(sRootTag + "_" + tag, content);
    }

    public static void e(String tag, String content, Exception e) {
        Log.d(sRootTag + "_" + tag, content + " e=" + e.getMessage());
    }
    // class DetailLogDemo

    /**
     * 打印一段字符串
     *
     * @param content
     */
    public static void printLog(String content) {
        Log.d(sRootTag, content);
    }

    /**
     * 打印线程名称
     */
    public static void printProcessName(Context context, String content) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                LogHelper.printLog(content + appProcess.processName);
            }
        }
    }

    /**
     * 得到调用此方法的线程的线程名
     *
     * @return
     */
    public static String getThreadName() {
        if (!sIsLogDebug) {
            return "";
        }
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

    public static void showToast(Context context, String content) {
        if (sIsToastDebug) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <pre>
     *  弹出一个Toast，主要用于非主线程中
     * </pre>
     *
     * @param context
     * @param content
     */
    public static void showToastOnUIThread(final Context context, final String content) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                if (sIsToastDebug) {
                    Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
