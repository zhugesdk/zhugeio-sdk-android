package com.zhuge.analysis.stat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无码打点配置文件
 * Created by jiaokang on 15/8/17.
 */
public class CodeLessConfig {
    private static final Object sInstanceLock = new Object();
    private static CodeLessConfig sInstance;


    private static boolean DEBUG = false;
    private static boolean mDisableGestureBindingUI = true;
    public static final int UI_FEATURES_MIN_API = 16;
    private Context mContext;
    public static HashMap<String, List<Integer>> viewTag = new HashMap<>();

    public void track(String eventName, JSONObject pro) {

        ZhugeSDK.getInstance().track(mContext, eventName, pro);
    }

    CodeLessConfig(Context context) {
        mContext = context;
    }

    public static String getEventUrl() {
        return "https://api.zhugeio.com/v1/events/codeless/appkey/";
    }

    public static String getEditorUrl() {
        return "ws://codeless.zhugeio.com/connect?ctype=client&platform=android&appkey=";
    }

    public static CodeLessConfig getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (null == sInstance) {
                final Context appContext = context.getApplicationContext();
                sInstance = new CodeLessConfig(appContext);
            }

            return sInstance;
        }
    }


    public Map<String, String> getDeviceInfo() {
        final Map<String, String> deviceInfo = new HashMap<String, String>();
        deviceInfo.put("android_lib_version", Constants.SDK_V);
        deviceInfo.put("android_os", "Android");
        deviceInfo.put("os_version", Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);
        deviceInfo.put("android_manufacturer", Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER);
        deviceInfo.put("android_brand", Build.BRAND == null ? "UNKNOWN" : Build.BRAND);
        deviceInfo.put("android_model", Build.MODEL == null ? "UNKNOWN" : Build.MODEL);
        try {
            final PackageManager manager = mContext.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            deviceInfo.put("app_version", info.versionName);
            deviceInfo.put("app_version_code", Integer.toString(info.versionCode));
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e("codeLess", "Exception getting app version name", e);
        }
        return Collections.unmodifiableMap(deviceInfo);
    }


    /*package*/
    static void openDebug() {
        CodeLessConfig.DEBUG = true;
    }

    public static boolean getDisableGestureBindingUI() {
        return mDisableGestureBindingUI;
    }

    /*package*/
    static void openGestureBindingUI() {
        mDisableGestureBindingUI = false;
    }


    public void debug(String message) {
        if (DEBUG) {
            Log.e("Zhuge.Codeless", message);
        }
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }
}
