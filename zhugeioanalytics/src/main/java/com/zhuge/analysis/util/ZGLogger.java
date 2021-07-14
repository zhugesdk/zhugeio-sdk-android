package com.zhuge.analysis.util;

import android.util.Log;

/**
 * 日志管理
 * Created by Omen on 16/8/31.
 */
public class ZGLogger {

    public static boolean logEnable = false;
    //默认输出级别info。
    private static int     logLevel = Log.INFO;

    public static void setLogLevel(int level){
        if (level > Log.ERROR){
            logLevel = Log.ERROR;
            return;
        }else if (level < Log.VERBOSE){
            logLevel = Log.VERBOSE;
            return;
        }
        logLevel = level;
    }

    public static void openLog(){
        logEnable = true;
    }

    public static void logError(String tag,String message){
        Log.e(tag,message);
    }

    public static void logMessage (String tag , String message){
        if (!logEnable){
            return;
        }
        switch (logLevel){
            case Log.ERROR:
                Log.e(tag,message);
                break;
            case Log.WARN:
                Log.w(tag,message);
                break;
            case Log.INFO:
                Log.i(tag,message);
                break;
            case Log.DEBUG:
                Log.d(tag,message);
                break;
            case Log.VERBOSE:
                Log.v(tag,message);
                break;
            default:
                Log.i(tag,message);
                break;
        }
    }
    public static void logVerbose(String mess){
        if (!logEnable){
            return;
        }
        Log.v("ZhugeLog",mess);
    }

    public static void handleException(String tag,String message , Throwable e){
        Log.e(tag,message,e);
    }

    /**
     * @param e Exception
     */
    public static void printStackTrace(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
