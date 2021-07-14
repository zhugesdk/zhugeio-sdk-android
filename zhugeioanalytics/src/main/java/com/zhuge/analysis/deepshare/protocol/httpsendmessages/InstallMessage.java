package com.zhuge.analysis.deepshare.protocol.httpsendmessages;

import android.content.Context;
import android.text.TextUtils;

import com.zhuge.analysis.BuildConfig;
import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.listeners.ZhugeInAppDataListener;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httprespmessages.InstallRespMessage;
import com.zhuge.analysis.stat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class InstallMessage extends ServerHttpSendJsonMessage {
    public final static String URL_PATH = "inappdata/";
    private ZhugeInAppDataListener listener;

    public InstallMessage(Context context, ZhugeInAppDataListener listener) {
        super(context);
        this.listener = listener;
    }

    public ZhugeInAppDataListener getListener() {
        return listener;
    }

    @Override
    public JSONObject getJSONObject(Configuration config) throws JSONException {
        JSONObject installPost = new JSONObject();

        installPost.put("is_newuser", true);

        String text = config.getClickId();
        config.setClickId("");
        if (!TextUtils.isEmpty(text)) {
            installPost.put("click_id", text);
        }

        text = config.getDeepLinkId();
        config.setDeepLinkId("");
        if (!TextUtils.isEmpty(text)) {
            installPost.put("deeplink_id", text);
        }

        text = config.getUniqueID();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("unique_id", text);
        }

        text = config.getHardwareId();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("hardware_id", text);
        }
        
        int versionCode = config.getAppVersionCode();
        installPost.put("app_version_code", versionCode);


        text = config.getAppVersion();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("app_version_name", text);
        }

        text = "android" + Constants.SDK_V;
        if (!TextUtils.isEmpty(text)) {
            installPost.put("sdk_info", text);
        }

        text = config.getCarrier();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("carrier_name", text);
        }

        installPost.put("is_wifi_connected", config.isWifiConnected());


        installPost.put("is_emulator", !config.hasRealHardwareId());

        text = config.getPhoneBrand();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("brand", text);
        }
        text = config.getPhoneModel();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("model", text);
        }

        text = config.getOS();
        if (!TextUtils.isEmpty(text)) {
            installPost.put("os", text);
        }

        installPost.put("os_version", "" + config.getOSRelease());
        return installPost;
    }

    @Override
    public ServerHttpRespMessage buildResponse() {
        return new InstallRespMessage(this);
    }

    @Override
    public String getUrlPath() {
        return URL_PATH + Configuration.getInstance().getAppKey();
    }

}