package com.zhuge.analysis.deepshare.protocol;

import android.content.Context;
import android.text.TextUtils;

import com.zhuge.analysis.BuildConfig;
import com.zhuge.analysis.deepshare.Configuration;
import com.zhuge.analysis.deepshare.ErrorString;
import com.zhuge.analysis.deepshare.utils.Log;
import com.zhuge.analysis.stat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

abstract public class ServerHttpSendJsonMessage extends ServerHttpSendMessage {
    private static final String TAG = "ServerHttpSendJsonMessage";
    private JSONObject obj = null;
    private Configuration config;

    public ServerHttpSendJsonMessage(Context context) {
        config = Configuration.getInstance(context);
    }

    public abstract JSONObject getJSONObject(Configuration config) throws JSONException;

    @Override
    public boolean prepare() {
        boolean success = false;
        if (TextUtils.isEmpty(config.getAppKey())) {
            getResponse().setError(ErrorString.ERR_NO_API_KEY);
        } else if (!config.isNetworkPermissionGranted()) {
            getResponse().setError(ErrorString.ERR_NETWORK_PERMISSION);
        } else {
            try {
                obj = getJSONObject(config);
                if (obj != null) {
                    success = true;
                }
            } catch (JSONException e) {
                if(BuildConfig.DEBUG){
                    Log.e(TAG, Log.getStackTraceString(e));
                }else{
                    Log.e(TAG, e.toString());
                }
            }
        }
        return success;
    }

    @Override
    public int send(OutputStream os) {
        int len = 0;
        if (obj != null) {
            DataOutputStream out = new DataOutputStream(os);
            try {
                obj.put("sdk_info", "android" + Constants.SDK_V);
                byte[] buff = obj.toString().getBytes("utf-8");
                out.write(buff);
                len = buff.length;
            } catch (IOException | JSONException e) {
                if(BuildConfig.DEBUG){
                    Log.e(TAG, Log.getStackTraceString(e));
                }else{
                    Log.e(TAG, e.toString());
                }
            }
        }
        return len;
    }

    @Override
    public void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("Content-type", "application/json");
    }

    @Override
    public String getMethod() {
        return ServerHttpSendMessage.POST;
    }

    @Override
    public String toString() {
        if (obj != null) {
            return obj.toString();
        }
        return super.toString();
    }
}
