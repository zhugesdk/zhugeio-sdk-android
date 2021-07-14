package com.zhuge.analysis.deepshare;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.zhuge.analysis.listeners.DSFailListener;
import com.zhuge.analysis.listeners.ZhugeInAppDataListener;
import com.zhuge.analysis.listeners.NewUsageFromMeListener;
import com.zhuge.analysis.deepshare.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Set;

public class DeepShare {
    private static final String TAG = "DeepShare";
    public static final int LINK_TYPE_UNLIMITED_USE = 0;

    /**
     * 初始化并返回程序启动参数
     * @param activity 当前activity
     * @param appId APP注册时生成的APP ID.
     * @param inappDataListener 用来接收inappData回调的instance.
     */
    public static void init(Activity activity, String appId, ZhugeInAppDataListener inappDataListener){
        if (instance == null) {
            instance = new DeepShareImpl(activity.getApplicationContext(), appId);
        }
        Uri data = activity.getIntent().getData();
        instance.initSession(inappDataListener, !instance.hasUser(), data, null);
        if (data != null && data.getQueryParameter("click_id") != null) {
            Uri.Builder builder = data.buildUpon().clearQuery();
            Set<String> names = data.getQueryParameterNames();
            for (String name : names) {
                if(!name.equals("click_id")) {
                    builder.appendQueryParameter(name, data.getQueryParameter(name));
                }
            }
            data = builder.build();
            activity.getIntent().setData(data);
        }
    }

    /**
     * 改变指定价值标签的值
     * @param tagToValue 所指定价值标签和其增加或减少的价值量所组成的HashMap.
     * @param callback 用户回调
     */
    public static void attribute(HashMap<String, Integer> tagToValue, DSFailListener callback){
        if (instance != null) {
            instance.changeValueBy(tagToValue, callback);
        } else {
            Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
        }
    }

    private static DeepShareImpl instance;

    /**
     *通知DeepShare停止会话，应该在Android的onStop()方法中调用
     */
    public static void onStop() {
        if (instance != null) {
            instance.close();
        } else {
            Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
        }
    }

    /**
     * 返回我的senderId
     * @return senderID
     */
    public static String getSenderId(){
        if (instance != null) {
            return instance.config.getUniqueID();
        } else {
            Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
            return null;
        }
    }

    /**
     * 获取channel信息
     *
     * @return channel
     */
    public static String[] getInstallChannels() {
        String[] channels = null;
        if (instance != null) {
            String channelsStr = instance.config.getInstallChannels();
            if (!TextUtils.isEmpty(channelsStr)) {
                try {
                    JSONArray channelsArray = new JSONArray(channelsStr);
                    int length = channelsArray.length();
                    if (length > 0) {
                        channels = new String[length];
                        for (int i = 0; i < length; ++i) {
                            channels[i] = channelsArray.getString(i);
                        }
                    }
                } catch (JSONException e) {
                }
            }
        } else {
            Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
        }
        return channels;
    }

    /**
     * 异步返回通过我的分享带来的此应用的新使用，包括新安装的用户量和再次激活打开的用户量
     *@param callback 用户回调
     */
     public static void getNewUsageFromMe(NewUsageFromMeListener callback){
         if (instance != null) {
             instance.getNewUsageFromMe(callback);
         } else {
             Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
         }
     }

    /**
     * 清空通过我的分享带来的此应用的新使用，包括新安装的用户量和再次激活打开的用户量
     * @param callback 用户回调
     */
    public static void clearNewUsageFromMe(DSFailListener callback){
        if (instance != null) {
            instance.clearUsageFromMe(callback);
        } else {
            Log.e(TAG, ErrorString.ERR_NOT_INITIALIZED);
        }
    }

}
