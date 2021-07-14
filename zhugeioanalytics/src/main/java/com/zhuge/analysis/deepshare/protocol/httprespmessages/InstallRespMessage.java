package com.zhuge.analysis.deepshare.protocol.httprespmessages;

import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.deepshare.protocol.ServerHttpRespJsonMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.InstallMessage;
import com.zhuge.analysis.deepshare.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstallRespMessage extends ServerHttpRespJsonMessage {
    private JSONObject data;

    public InstallRespMessage(ServerHttpSendJsonMessage sent) {
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
        if (data.length() > 0){
            data.put("tag_deep",1);//deepShare 下载打开
        }else {
            data.put("tag_deep",3); //首次打开
        }

        if (obj.has("channels")) {
            JSONArray channels = obj.getJSONArray("channels");
            if (channels != null) {
                if (data!=null){
                    String o = channels.optString(0);
                    data.put("channel",o);
                }
                Configuration.getInstance().setInstallChannels(String.valueOf(channels));
            }
        }
    }

    @Override
    public void processResponse() {
        if (isOk()) {
            Configuration.getInstance().setClickId("");

            if (((InstallMessage) getRequest()).getListener() != null) {
                long currentTime = System.currentTimeMillis();
                Util.inappDataTime.put(currentTime - Util.startTicks);
                ((InstallMessage) getRequest()).getListener().zgOnInAppDataReturned(getParams());
            }

        } else if (((InstallMessage) getRequest()).getListener() != null) {
            ((InstallMessage) getRequest()).getListener().zgOnFailed(getError());
        }
    }

    public JSONObject getParams() {
        return data;
    }

}
