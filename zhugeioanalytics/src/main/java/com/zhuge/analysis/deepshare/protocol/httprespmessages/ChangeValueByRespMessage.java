package com.zhuge.analysis.deepshare.protocol.httprespmessages;

import com.zhuge.analysis.deepshare.protocol.ServerHttpRespJsonMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.ChangeValueByMessage;
import com.zhuge.analysis.deepshare.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeValueByRespMessage extends ServerHttpRespJsonMessage {

    public ChangeValueByRespMessage(ServerHttpSendJsonMessage sent) {
        super(sent);
    }


    @Override
    public void getPayload(JSONObject obj) throws JSONException {

    }

    @Override
    public void processResponse() {
        if (isOk()) {
            //TODO:Should have a success callback
            long currentTime = System.currentTimeMillis();
            Util.attributeTime.put(currentTime - Util.startTicks);
        } else if (((ChangeValueByMessage) getRequest()).getListener() != null) {
            ((ChangeValueByMessage) getRequest()).getListener().zgOnFailed(getError());
        }
    }

}
