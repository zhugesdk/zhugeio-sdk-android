package com.zhuge.analysis.stat.exp.utils;

/**
 * Created by Sure on 2021/6/23.
 */

import android.os.Handler;
import android.os.Looper;

public class ThreadUtil {
    private static final Object lock = new Object();
    private static Handler uiThreadHandler = null;

    public ThreadUtil() {
    }


    private static Handler getUiThreadHandler() {
        if (uiThreadHandler != null) {
            return uiThreadHandler;
        }
        synchronized (lock) {
            if (uiThreadHandler == null) {
                uiThreadHandler = new Handler(Looper.getMainLooper());
            }
            return uiThreadHandler;
        }
    }

    public static void runOnUiThread(Runnable r) {
        if (isRunningOnUiThread()) {
            r.run();
        } else {
            getUiThreadHandler().post(r);
        }

    }

    public static void postOnUiThread(Runnable task) {
        getUiThreadHandler().post(task);
    }


    public static void cancelTaskOnUiThread(Runnable task) {
        getUiThreadHandler().removeCallbacks(task);
    }

    public static boolean isRunningOnUiThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static Looper getUiThreadLooper() {
        return getUiThreadHandler().getLooper();
    }
}
