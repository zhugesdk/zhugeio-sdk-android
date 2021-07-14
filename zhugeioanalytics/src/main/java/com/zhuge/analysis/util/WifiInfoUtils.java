package com.zhuge.analysis.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by kongmiao on 14-4-15.
 * 网络mac信息工具类
 */
public class WifiInfoUtils {
    private WifiManager wifiManager;

    public WifiInfoUtils(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

}
