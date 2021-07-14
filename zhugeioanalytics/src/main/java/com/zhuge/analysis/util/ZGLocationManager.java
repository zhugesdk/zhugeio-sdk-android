package com.zhuge.analysis.util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class ZGLocationManager {
//    private WifiManager wifiManager;
//
//    public WifiInfoUtils(Context context) {
//        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//    }
//
//    public String getMacAddress() {
//        WifiInfo info = this.wifiManager.getConnectionInfo();
//        if (info!= null){
//            return info.getMacAddress();
//        }
//        return null;
//    }


    String TAG = "FLY.LocationUtils";

    private volatile static ZGLocationManager manager;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context mContext;

    public ZGLocationManager(Context context) {
        mContext = context;

    }

    public static ZGLocationManager getInstance(Context context) {

        if (manager == null) {
            synchronized (ZGLocationManager.class) {
                if (manager == null) {
                    manager = new ZGLocationManager(context);
                }
            }
        }
        return  manager;
    }





}
