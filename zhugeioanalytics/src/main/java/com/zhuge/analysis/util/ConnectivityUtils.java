package com.zhuge.analysis.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态管理类
 * Created by kongmiao on 14-10-11.
 */
public class ConnectivityUtils {
    private ConnectivityManager connectivityManager;

    public ConnectivityUtils(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取当前活动的网络类型，由于后台数据库错误，当是wifi时，需要转换为4.
     * @return 当前网络类型，4是WiFi
     */
    public int getNetworkType() {
        try {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int i = networkInfo.getType();
                if (i == ConnectivityManager.TYPE_WIFI){
                    return 4;
                }
                return i;
            }
        }catch (Exception e){
            ZGLogger.handleException("com.zhuge.Connective", "获取活动网络类型出错",e);
        }
        return -100;
    }
    public boolean isOnline() {
        boolean isOnline;
        try {
            final NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            isOnline = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            isOnline = true;
            ZGLogger.handleException("com.zhuge.Connective", "没有权限检查网络。假定网络可用。",e);
        }
        return isOnline;
    }
}
