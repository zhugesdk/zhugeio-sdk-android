package com.zhuge.analysis.deepshare;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.zhuge.analysis.listeners.DSFailListener;
import com.zhuge.analysis.listeners.ZhugeInAppDataListener;
import com.zhuge.analysis.listeners.NewUsageFromMeListener;
import com.zhuge.analysis.deepshare.protocol.ServerMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.ChangeValueByMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.ClearUsageMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.CloseMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.InstallMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.NewUsageMessage;
import com.zhuge.analysis.deepshare.protocol.httpsendmessages.OpenMessage;
import com.zhuge.analysis.deepshare.transport.ServerHttpConnection;
import com.zhuge.analysis.deepshare.utils.Util;

import java.util.HashMap;

/**
 * Created by joy on 15/9/3.
 */
public class DeepShareImpl {
    private static final String TAG = "DeepShareImpl";

    final Configuration config;
    private final Context context;


    DeepShareImpl(Context context, String appId) {
        this.context = context;
        this.config = Configuration.getInstance(context.getApplicationContext());

        ServerMessageMgr.getInstance().registerHandler(handler);

        config.setAppKey(appId);
    }


    private UiServerMessageHandler handler = new UiServerMessageHandler() {
        @Override
        protected void processEvent(ServerMessage msg) {
            msg.processResponse();
        }
    };

    boolean initSession(ZhugeInAppDataListener callback, boolean isReferrable, Uri data, String key) {
        Util.initUtil();
        Util.startTicks = System.currentTimeMillis();

        boolean uriHandled = false;
        if (data != null && data.isHierarchical()) {
            if (data.getQueryParameter("click_id") != null) {
                uriHandled = true;
                config.setClickId(data.getQueryParameter("click_id"));
            }
            if (data.getQueryParameter("deeplink_id") != null) {
                config.setDeepLinkId(data.getQueryParameter("deeplink_id"));
            }
            if (data.getQueryParameter("is_scheme") != null) {
                config.setScheme(data.getQueryParameter("is_scheme"));
            }
        } else {
            config.setClickId("");
        }

        ServerHttpConnection.reset(1);

        if (hasUser()) {
            ServerHttpConnection.send(new OpenMessage(context, callback));
        } else {
            ServerHttpConnection.send(new InstallMessage(context, callback));
        }

        config.setInitKey("Initialized");

        if (key != null) {
            config.setAppKey(key);
        }

        return uriHandled;
    }

    boolean hasUser() {
        return !TextUtils.isEmpty(config.getInitKey());
    }

    void close(){
        ServerHttpConnection.send(new CloseMessage(context));
    }

    void getNewUsageFromMe(NewUsageFromMeListener callback){
        ServerHttpConnection.send(new NewUsageMessage(context, callback));
    }

    void clearUsageFromMe(DSFailListener callback){
        ServerHttpConnection.send(new ClearUsageMessage(context, callback));
    }

    void changeValueBy(HashMap<String, Integer> tagToValue, DSFailListener callback) {
        //JSONObject tagToValues = new JSONObject(tagToValue);
        Util.startTicks = System.currentTimeMillis();
        ServerHttpConnection.send(new ChangeValueByMessage(context, tagToValue, callback));
    }


}
