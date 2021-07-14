package com.zhuge.analysis.deepshare.protocol;

import com.zhuge.analysis.listeners.utils.Base64;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

abstract public class ServerHttpSendMessage extends ServerMessage {
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";
    ServerHttpRespMessage response;
    private String username = null;
    private String password = null;

    public ServerHttpSendMessage() {
        init();
    }

    public ServerHttpSendMessage(String username, String password) {
        init();
        this.username = username;
        this.password = password;
    }

    public abstract boolean prepare();

    public abstract int send(OutputStream os);

    public abstract ServerHttpRespMessage buildResponse();

    public abstract String getUrlPath();

    public abstract String getMethod();

    public void setRequestProperty(HttpURLConnection conn) {
        if (username != null && username.length() > 0
                && password != null && password.length() > 0) {
            byte[] token;
            try {
                token = (username + ":" + password).getBytes("utf-8");
                String authorization = "Basic " + new String(Base64.encode(token, Base64.NO_WRAP), "utf-8");
                conn.setRequestProperty("Authorization", authorization);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
    }

    public ServerHttpRespMessage getResponse() {
        return response;
    }

    private void init() {
        direction = ServerMessage.DIRECTION_OUT;
        response = buildResponse();
    }
}
