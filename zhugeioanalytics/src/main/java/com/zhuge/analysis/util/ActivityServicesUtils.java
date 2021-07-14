package com.zhuge.analysis.util;

import android.app.ActivityManager;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Acitivity管理类
 * Created by kongmiao on 14-10-13.
 */
public class ActivityServicesUtils {
    private ActivityManager activityManager;

    public ActivityServicesUtils(Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }


    /*
     * 获取当前进程名称
     */
    public String getMyProcessName() {

        int pid = android.os.Process.myPid();

        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (null == runningAppProcesses){
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }catch (Exception e){
            ZGLogger.handleException("com.zhuge.AS","获取进程名称出错。",e);
        }
        return "";
    }
}
