package com.zhuge.analysis.deepshare;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

public class Configuration {
    private static final String TAG = "Configuration";
    private static final String SHARED_PREF_FILE = "deep_share_preference";

    private static final String KEY_APP_KEY = "app_key";
    private static final String KEY_INIT_KEY = "init_key";
    private static final String KEY_ANDROID_ID = "android_id";
    private static final String KEY_LINK_CLICK_IDENTIFIER = "link_click_identifier";
    private static final String KEY_INSTALL_CHANNELS = "install_channels";
    private static final String KEY_DEEP_LINK_IDENTIFIER = "link_deep_link_identifier";
    private static final String KEY_SCHEME_IDENTIFIER = "link_scheme";

    private static Configuration config;
    private SharedPreferences preference;

    private Context context;

    private Configuration(Context context) {
        preference = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        this.context = context;
    }

    public static Configuration getInstance(Context context) {
        if (config == null) {
            config = new Configuration(context);
        }
        return config;
    }

    public static Configuration getInstance() {
        return config;
    }

    public String getAppKey() {
        String appKey = getString(KEY_APP_KEY);

        if (TextUtils.isEmpty(appKey)) {
            try {
                final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (ai.metaData != null) {
                    appKey = ai.metaData.getString("com.zhuge.analysis.APP_KEY");
                }
            } catch (final PackageManager.NameNotFoundException e) {
            }
        }

        return appKey;
    }

    public void setAppKey(String key) {
        setString(KEY_APP_KEY, key);
    }

    public String getInitKey() {
        return getString(KEY_INIT_KEY);
    }

    public void setInitKey(String key) {
        setString(KEY_INIT_KEY, key);
    }

    public String getClickId() {
        return getString(KEY_LINK_CLICK_IDENTIFIER);
    }

    public void setClickId(String identifier) {
        setString(KEY_LINK_CLICK_IDENTIFIER, identifier);
    }

    public String getInstallChannels() {
        return getString(KEY_INSTALL_CHANNELS);
    }

    public void setInstallChannels(String installChannel) {
        setString(KEY_INSTALL_CHANNELS, installChannel);
    }

    public String getDeepLinkId() {
        return getString(KEY_DEEP_LINK_IDENTIFIER);
    }

    public void setDeepLinkId(String identifier) {
        setString(KEY_DEEP_LINK_IDENTIFIER, identifier);
    }

    public String getScheme() {
        return getString(KEY_SCHEME_IDENTIFIER);
    }

    public void setScheme(String identifier) {
        setString(KEY_SCHEME_IDENTIFIER, identifier);
    }

    private String getString(String key) {
        return preference.getString(key, null);
    }

    private void setString(String key, String value) {
        preference.edit().putString(key, value).commit();
    }

    public String getUniqueID() {
        String androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        if (androidID == null) {
            if (getString(KEY_ANDROID_ID) == null) {
                androidID = UUID.randomUUID().toString();
                setString(KEY_ANDROID_ID, androidID);
            }
            else {
                androidID = getString(KEY_ANDROID_ID);
            }
        }

        return androidID;
    }

    public String getHardwareId() {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public boolean hasRealHardwareId() {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID) != null;
    }

    public String getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionName != null) {
                return packageInfo.versionName;
            } else {
                return null;
            }
        } catch (NameNotFoundException ignored) {
        }
        return null;
    }

    public int getAppVersionCode() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionCode != 0) {
                return packageInfo.versionCode;
            } else {
                return 0;
            }
        } catch (NameNotFoundException ignored) {
        }
        return 0;
    }

    public String getCarrier() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            String ret = telephonyManager.getNetworkOperatorName();
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    public String getPhoneBrand() {
        return android.os.Build.MANUFACTURER;
    }

    public String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public String getOS() {
        return "Android";
    }

    public String getOSRelease() { return android.os.Build.VERSION.RELEASE; }

    public boolean isWifiConnected() {
        if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        }
        return false;
    }

    public boolean isNetworkPermissionGranted() {
        return PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
    }
}
