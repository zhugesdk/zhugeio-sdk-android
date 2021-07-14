package com.zhuge.analysis.deepshare;

import android.os.Handler;
import android.os.Looper;

import com.zhuge.analysis.deepshare.protocol.ServerMessage;

import java.util.ArrayList;

public abstract class UiServerMessageHandler implements ServerMessageHandler {
    protected Handler localHandler;

    public UiServerMessageHandler() {
        localHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void handleServerMessage(ArrayList<ServerMessage> msgs) {
        localHandler.post(new MyRunnable(msgs));
    }

    protected abstract void processEvent(ServerMessage msg);

    private class MyRunnable implements Runnable {
        private final ArrayList<ServerMessage> msgs;

        public MyRunnable(ArrayList<ServerMessage> msgs) {
            this.msgs = msgs;
        }

        @Override
        public void run() {
            for (ServerMessage msg : msgs) {
                processEvent(msg);
            }
        }
    }
}
