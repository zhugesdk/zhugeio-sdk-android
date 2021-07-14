package com.zhuge.analysis.deepshare;

import android.os.Handler;
import android.os.HandlerThread;

import com.zhuge.analysis.deepshare.protocol.ServerMessage;
import com.zhuge.analysis.deepshare.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ServerMessageMgr {

    private static final String TAG = "ServerMessageMgr";
    private static ServerMessageMgr instance = new ServerMessageMgr();
    private final Handler localHandler;
    private final HandlerThread handlerThread;

    private final Map<Class<? extends ServerMessage>, HashSet<ServerMessageHandler>> handlerMap;
    private ServerMessageHandler handler;
    private final FireRunnable fireRunnable;
    private ArrayList<ServerMessage> pendingList;

    private ServerMessageMgr() {
        handlerMap = new HashMap<>();
        pendingList = new ArrayList<>();

        handlerThread = new HandlerThread("ServerMessageThread");
        handlerThread.start();
        localHandler = new Handler(handlerThread.getLooper());
        fireRunnable = new FireRunnable();
    }

    public static ServerMessageMgr getInstance() {
        return instance;
    }

    public synchronized void registerHandler(Class<? extends ServerMessage> clazz, ServerMessageHandler updater) {
        if (!handlerMap.containsKey(clazz)) {
            handlerMap.put(clazz, new HashSet<ServerMessageHandler>());
        }
        handlerMap.get(clazz).add(updater);
    }

    public synchronized void registerHandler(ServerMessageHandler updater) {
        handler = updater;
    }

    public synchronized void unregisterHandler(Class<? extends ServerMessage> clazz, ServerMessageHandler updater) {
        if (handlerMap.containsKey(clazz)
                && handlerMap.get(clazz).contains(updater)) {
            handlerMap.get(clazz).remove(updater);
            if (handlerMap.get(clazz).isEmpty()) {
                handlerMap.remove(clazz);
            }
        }
    }

    public void reset() {
        localHandler.removeCallbacks(fireRunnable);
    }

    public synchronized void fireServerMessage(final ServerMessage msg) {
        if (msg.isSessionOutDate()) {
            return;
        }
        pendingList.add(msg);
        localHandler.removeCallbacks(fireRunnable);
        localHandler.post(fireRunnable);
    }

    private class FireRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (ServerMessageMgr.this) {
                Log.d(TAG, "FireRunnable processing " + pendingList.size() + " msgs");
                HashMap<ServerMessageHandler, ArrayList<ServerMessage>> dispatchMap = new HashMap<>();
                ArrayList<ServerMessage> remainingList = new ArrayList<>();
                for (ServerMessage msg : pendingList) {
                    if (msg.isSessionOutDate()) {
                        continue;
                    }

                    boolean handled = false;

                    if (handler != null) {
                        if (dispatchMap.get(handler) == null) {
                            dispatchMap.put(handler, new ArrayList<ServerMessage>());
                        }
                        dispatchMap.get(handler).add(msg);
                        handled = true;
                    }

                    if (!handled) {
                        Log.e(TAG, "WARNING: an event was fired but no handler (" + msg.getRecvRetry() + ")" + msg.getClass().getSimpleName() + " : " + msg.toString());
                        if (msg.nextRecvRetry()) {
                            remainingList.add(msg);
                        }
                    }
                }

                for (ServerMessageHandler handler : dispatchMap.keySet()) {
                    if (!(handler instanceof UiServerMessageHandler)) {
                        handler.handleServerMessage(dispatchMap.get(handler));
                    }
                }
                for (ServerMessageHandler handler : dispatchMap.keySet()) {
                    if (handler instanceof UiServerMessageHandler) {
                        handler.handleServerMessage(dispatchMap.get(handler));
                    }
                }

                pendingList = remainingList;

                if (pendingList.size() != 0) {
                    localHandler.removeCallbacks(fireRunnable);
                    localHandler.postDelayed(fireRunnable, 500);
                }
            }
        }
    }
}
