package com.zhuge.analysis.deepshare.protocol;

public class ServerNetworkMessage extends ServerMessage {

    public ServerNetworkMessage() {
        direction = ServerMessage.DIRECTION_NONE;
    }

    @Override
    public int getRecvRetry() {
        return Integer.MAX_VALUE;
    }
}
