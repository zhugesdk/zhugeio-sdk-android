package com.zhuge.analysis.deepshare.protocol.httpsendmessages;

import android.content.Context;
import android.text.TextUtils;

import com.zhuge.analysis.BuildConfig;
import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.listeners.ZhugeInAppDataListener;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httprespmessages.OpenRespMessage;
import com.zhuge.analysis.stat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class OpenMessage extends ServerHttpSendJsonMessage {
    public final static String URL_PATH = "inappdata/";
    private ZhugeInAppDataListener listener;

    public OpenMessage(Context context, ZhugeInAppDataListener listener) {
        super(context);
        this.listener = listener;
    }

    public ZhugeInAppDataListener getListener() {
        return listener;
    }

    @Override
    public JSONObject getJSONObject(Configuration config) throws JSONException {
        JSONObject openPost = new JSONObject();

        openPost.put("is_newuser", false);

        String text = config.getClickId();
        config.setClickId("");
        if (!TextUtils.isEmpty(text)) {
            openPost.put("click_id", text);;
        }

        text = config.getDeepLinkId();
        config.setDeepLinkId("");
        if (!TextUtils.isEmpty(text)) {
            openPost.put("deeplink_id", text);
            ;
        }

        text = config.getScheme();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("is_scheme", text);
        }

        text = config.getUniqueID();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("unique_id", text);
        }
        int versionCode = config.getAppVersionCode();
        openPost.put("app_version_code", versionCode);


        text = config.getAppVersion();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("app_version_name", text);
        }

        text = "android" + Constants.SDK_V;
        if (!TextUtils.isEmpty(text)) {
            openPost.put("sdk_info", text);
        }

        text = config.getCarrier();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("carrier_name", text);
        }

        openPost.put("is_wifi_connected", config.isWifiConnected());

        openPost.put("is_emulator", !config.hasRealHardwareId());

        text = config.getPhoneBrand();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("brand", text);
        }
        text = config.getPhoneModel();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("model", text);
        }

        text = config.getOS();
        if (!TextUtils.isEmpty(text)) {
            openPost.put("os", text);
        }

        openPost.put("os_version", "" + config.getOSRelease());

        return openPost;
    }

    @Override
    public ServerHttpRespMessage buildResponse() {
        return new OpenRespMessage(this);
    }

    @Override
    public String getUrlPath() {
        return URL_PATH + Configuration.getInstance().getAppKey();
    }
}
