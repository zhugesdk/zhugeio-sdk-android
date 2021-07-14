package com.zhuge.analysis.deepshare.protocol.httpsendmessages;

import android.content.Context;

import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.listeners.DSFailListener;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httprespmessages.ChangeValueByRespMessage;
import com.zhuge.analysis.deepshare.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map.Entry;

public class ChangeValueByMessage extends ServerHttpSendJsonMessage {
    public final static String URL_PATH = "counters/";
    private JSONObject tagedValues;
    private JSONObject receiverInfo = new JSONObject();
    private JSONArray countersArray = new JSONArray();
    private DSFailListener listener;

    public ChangeValueByMessage(Context context, JSONObject tagValues, DSFailListener listener) {
        super(context);
        this.tagedValues = tagValues;
        this.listener = listener;
    }

    public ChangeValueByMessage(Context context, HashMap<String, Integer> tagToValue, DSFailListener listener) {
        super(context);
        try {
            this.receiverInfo.put("unique_id", Configuration.getInstance().getUniqueID());
            for (Entry<String, Integer> entry : tagToValue.entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("event", entry.getKey());
                jsonObject.put("count", entry.getValue());
                this.countersArray.put(Util.escapeJSONStrings(jsonObject));
            }

        } catch (JSONException ex) {
        }
        this.listener = listener;
    }

    @Override
    public JSONObject getJSONObject(Configuration config) throws JSONException {
        JSONObject linkPost = new JSONObject();

        linkPost.put("receiver_info", Util.escapeJSONStrings(receiverInfo));
        linkPost.put("counters", countersArray);

        return linkPost;
    }

    public DSFailListener getListener() {
        return listener;
    }

    @Override
    public ServerHttpRespMessage buildResponse() {
        return new ChangeValueByRespMessage(this);
    }

    @Override
    public String getUrlPath() {
        return URL_PATH + Configuration.getInstance().getAppKey();
    }

}
