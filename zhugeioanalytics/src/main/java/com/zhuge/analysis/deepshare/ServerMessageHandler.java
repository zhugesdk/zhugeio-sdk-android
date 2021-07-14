package com.zhuge.analysis.deepshare;

import com.zhuge.analysis.deepshare.protocol.ServerMessage;

import java.util.ArrayList;

public interface ServerMessageHandler {
    public abstract void handleServerMessage(ArrayList<ServerMessage> msgs);
}