package com.zhuge.analysis.deepshare.protocol.httpsendmessages;

import android.content.Context;

import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.listeners.NewUsageFromMeListener;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendMessage;
import com.zhuge.analysis.deepshare.protocol.httprespmessages.NewUsageRespMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class NewUsageMessage extends ServerHttpSendJsonMessage {
    public final static String URL_PATH = "dsusages/";
    private NewUsageFromMeListener listener;

    public NewUsageMessage(Context context, NewUsageFromMeListener callback) {
        super(context);
        this.listener = callback;
    }

    @Override
    public JSONObject getJSONObject(Configuration config) throws JSONException {

        JSONObject linkPost = new JSONObject();

        //linkPost.put("session_id", config.getUniqueID());

        return linkPost;
    }

    public NewUsageFromMeListener getListener() {
        return listener;
    }

    @Override
    public ServerHttpRespMessage buildResponse() {
        return new NewUsageRespMessage(this);
    }

    @Override
    public String getMethod() {
        return ServerHttpSendMessage.GET;
    }

    @Override
    public String getUrlPath() {
        return URL_PATH + Configuration.getInstance().getAppKey() + "/" + Configuration.getInstance().getUniqueID();
    }

}
