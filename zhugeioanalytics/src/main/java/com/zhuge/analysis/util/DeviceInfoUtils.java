package com.zhuge.analysis.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


/**
 * 设备信息管理类
 * Created by kongmiao on 14-10-11.
 */
public class DeviceInfoUtils {

    /*
     * 获取操作系统版本号
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /*
     * 获取生产厂商
     */
    public static String getManfacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /*
     * 获取品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /*
     * 获取设备型号
     */
    public static String getDevice() {
        return android.os.Build.MODEL;
    }


    /*
     * 获取分辨率
     */
    public static String getResolution(final Context context) {
        // user reported NPE in this method; that means either getSystemService or getDefaultDisplay
        // were returning null, even though the documentation doesn't say they should do so; so now
        // we catch Throwable and return empty string if that happens
        StringBuilder resolution = new StringBuilder();
        try {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm == null){
                return resolution.append("null").toString();
            }
            final Display display = wm.getDefaultDisplay();
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            resolution.append(metrics.heightPixels)
                    .append("x")
                    .append(metrics.widthPixels);
        } catch (Exception e) {
            Log.e("ZhugeSDK", "没有检测到分辨率");
        }
        return resolution.toString();
    }
    public static float[] getScreenDensity(Context context){
        try {
            Resources resources = context.getResources();
            int status_bar = resources.getIdentifier("status_bar_height", "dimen", "android");
            int dimensionPixelSize = resources.getDimensionPixelSize(status_bar);
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            final Display display = wm.getDefaultDisplay();
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            float density = metrics.density;
            int widthPixels = metrics.widthPixels;
            int heightPixels = metrics.heightPixels - dimensionPixelSize;
            float width =  widthPixels/density;
            float height = heightPixels/density;
            return new float[]{width,height,density};
        } catch (Exception e) {
            Log.e("ZhugeSDK", "检测屏幕dpi出错");
        }
        return null;
    }

}
