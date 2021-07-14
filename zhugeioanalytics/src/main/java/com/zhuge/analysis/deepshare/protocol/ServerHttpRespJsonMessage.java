package com.zhuge.analysis.deepshare.protocol;

import com.zhuge.analysis.BuildConfig;
import com.zhuge.analysis.deepshare.ErrorString;
import com.zhuge.analysis.deepshare.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

abstract public class ServerHttpRespJsonMessage extends ServerHttpRespMessage {
    private static final String TAG = "ServerHttpRespJsonMessage";

    private JSONObject obj = null;

    public ServerHttpRespJsonMessage(ServerHttpSendJsonMessage sent) {
        super(sent);
    }

    @Override
    public void receive(byte[] bytes) {
        if (isOk()) {
            if (bytes != null) {
                String err = null;
                try {
                    if (bytes.length != 0) {
                        String str = new String(bytes, "UTF-8");
                        if(isClientBadRequest()) {
                            err = str;
                        }else {
                            JSONTokener jsonParser = new JSONTokener(str);
                            Object o = jsonParser.nextValue();
                            if (o != null && o instanceof JSONObject) {
                                obj = (JSONObject) o;
                                getPayload(obj);
                                err = null;
                            } else {
                                getPayload(new JSONObject());
                                err = null;
                            }
                        }
                    }
                } catch (JSONException | UnsupportedEncodingException e) {
                    if(BuildConfig.DEBUG){
                        Log.e(TAG, Log.getStackTraceString(e));
                    }else{
                        Log.e(TAG, e.toString());
                    }
                    err = ErrorString.ERR_JSON;
                }
                setError(err);
            } else {
                setError(ErrorString.ERR_HTTP);
            }
        }
    }

    private String getErrorReason(JSONObject obj) throws JSONException {
        if (obj.has("error")) {
            return obj.getString("error");
        }
        return null;
    }

    abstract public void getPayload(JSONObject obj) throws JSONException;

    @Override
    public String toString() {
        if (obj != null) {
            return obj.toString();
        }
        return super.toString();
    }

}
