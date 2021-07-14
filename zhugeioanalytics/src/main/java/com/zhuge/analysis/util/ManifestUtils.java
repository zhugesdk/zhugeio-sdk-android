package com.zhuge.analysis.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 获取Manifest文件信息
 * Created by Omen on 15/11/9.
 */
public class ManifestUtils {

    /**
     * 获取开发者配置信息
     *
     * @param context 应用上下文
     * @return AppKey 与Channel信息组成的字符串，若出错或未填写返回空字符串
     */
    public static String[] getManifestInfo(Context context) {
        String[] devInfo;
        String ak;
        String cn;
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            ak = appInfo.metaData.getString("ZHUGE_APPKEY");
            cn = appInfo.metaData.getString("ZHUGE_CHANNEL");
            devInfo = new String[]{ak, cn};
        } catch (Exception e) {
            ZGLogger.handleException(TAG, "获取应用信息失败，请检查Manifest，确保设置ZHUGE_APPKEY和ZHUGE_CHANNEL",e);
            return new String[]{"null", "null"};
        }
        return devInfo;
    }

    private static final String TAG = "ZhugeSDK.Manifest";
}
