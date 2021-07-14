package com.zhuge.analysis.deepshare.protocol;

public class ServerMessage {
    public static final int DIRECTION_OUT = 1;
    public static final int DIRECTION_IN = 2;
    public static final int DIRECTION_NONE = 3;
    private static volatile int currentSession = 0;
    protected int direction;
    private int recvRetry = 0;
    private int sendRetry = 0;
    private int session;

    public ServerMessage() {
        saveSession();
    }

    public static synchronized void newSession() {
        currentSession++;
    }

    public boolean isOut() {
        return DIRECTION_OUT == direction;
    }

    public boolean isIn() {
        return DIRECTION_IN == direction;
    }

    public synchronized boolean nextRecvRetry() {
        if (recvRetry > 20) {
            return false;
        }
        recvRetry++;
        return true;
    }

    public synchronized int getRecvRetry() {
        return recvRetry;
    }

    public synchronized boolean nextSendRetry() {
        if (sendRetry > 0) {
            return false;
        }
        sendRetry++;
        return true;
    }

    public synchronized int getSendRetry() {
        return sendRetry;
    }

    public synchronized void resetSendRetry() {
        sendRetry = 0;
    }

    public void saveSession() {
        session = currentSession;
    }

    public boolean isSessionOutDate() {
        return currentSession != session;
    }

    public void processResponse() {}
}
