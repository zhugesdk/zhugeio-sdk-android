package com.zhuge.analysis.deepshare.protocol.httprespmessages;

import com.zhuge.analysis.deepshare.protocol.ServerHttpRespJsonMessage;
import com.zhuge.analysis.deepshare.protocol.ServerHttpSendJsonMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.ClearUsageMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class ClearUsageRespMessage extends ServerHttpRespJsonMessage {


    public ClearUsageRespMessage(ServerHttpSendJsonMessage sent) {
        super(sent);
    }


    @Override
    public void getPayload(JSONObject obj) throws JSONException {
    }

    @Override
    public void processResponse() {
        if (isOk()) {
            //TODO:Should have a success callback
        } else if (((ClearUsageMessage) getRequest()).getListener() != null) {
            ((ClearUsageMessage) getRequest()).getListener().zgOnFailed(getError());
        }
    }
}
