package com.zhuge.analysis.java_websocket.handshake;

public interface ServerHandshake extends Handshakedata {
    public short getHttpStatus();

    public String getHttpStatusMessage();
}
