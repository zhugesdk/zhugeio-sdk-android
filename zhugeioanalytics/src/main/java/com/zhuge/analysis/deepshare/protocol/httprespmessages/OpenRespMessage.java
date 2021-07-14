package com.zhuge.analysis.deepshare.protocol.httprespmessages;

import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespJsonMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.OpenMessage;
import com.zhuge.analysis.deepshare.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenRespMessage extends ServerHttpRespJsonMessage {
    private JSONObject data;

    public OpenRespMessage(ServerHttpSendJsonMessage sent) {
        super(sent);
    }


    @Override
    public void getPayload(JSONObject obj) throws JSONException {
        if (obj.has("inapp_data")) {
            data = Util.getJSONObject(obj.getString("inapp_data"));
        }
        if (data == null){
            data = new JSONObject();
        }
        if (data .length() > 0){
            data.put("tag_deep",0); //deepShare唤醒打开
        }else {
            data.put("tag_deep",2);//直接打开
        }
        if (obj.has("channels")) {
            JSONArray channels = obj.getJSONArray("channels");
            if (data!=null){
                String o = channels.optString(0);
                data.put("channel",o);
            }
        }
    }

    @Override
    public void processResponse() {
        if (isOk()) {
            Configuration.getInstance().setClickId("");

            if (((OpenMessage) getRequest()).getListener() != null) {
                long currentTime = System.currentTimeMillis();
                Util.inappDataTime.put(currentTime - Util.startTicks);
                ((OpenMessage) getRequest()).getListener().zgOnInAppDataReturned(getParams());
            }

        } else if (((OpenMessage) getRequest()).getListener() != null) {
            ((OpenMessage) getRequest()).getListener().zgOnFailed(getError());
        }
    }

    public JSONObject getParams() {
        return data;
    }
}
