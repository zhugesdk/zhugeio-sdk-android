package com.zhuge.analysis.deepshare.protocol;

import com.zhuge.analysis.deepshare.utils.Log;

public class ServerNetworkError extends ServerNetworkMessage {
    public static final int HTTP_JSON_MESSAGE = 1;
    public static final int NET_UNAVAILABLE_WHEN_SEND_MESSAGE = 2;
    public static final int HTTP_OTHER_MESSAGE = 3;
    public static final int HTTP_NETWORK_RESET = 4;
    private final int why, subwhy;
    private final ServerMessage sent;

    public ServerNetworkError(ServerMessage sent, int why, int subwhy) {
        this.why = why;
        this.subwhy = subwhy;
        this.sent = sent;
    }

    public ServerMessage getRequest() {
        return sent;
    }

    public int getWhy() {
        return why;
    }

    public int getSubWhy() {
        return subwhy;
    }

    @Override
    public String toString() {
        return "ServerNetworkError " + why + " " + subwhy;
    }

    @Override
    public void processResponse() {
        Log.e("DeepShareImpl", "Network error " + getWhy() + " " + getSubWhy());
    }
}
