package com.zhuge.zhugeiodemo.Utils;

import android.content.Context;

//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.zhuge.analysis.stat.ZhugeParam;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONObject;

public class Tracker {


    public static void setupZhugeioAnalytics(Context context) {

        ZhugeParam params = new ZhugeParam.Builder()
                .appKey("d76f58b1019e46e59644d5801295384f")
                .appChannel("zhugeio-demo")
                .build();
//        ZhugeSDK.getInstance().openAutoTrack();
        ZhugeSDK.getInstance().enableJavaScriptBridge();
        ZhugeSDK.getInstance().enableExpTrack();
        ZhugeSDK.getInstance().openLog();
        ZhugeSDK.getInstance().openVisual();
        ZhugeSDK.getInstance().openVisualDebug();
        ZhugeSDK.getInstance().setupCodelessUrl("ws://112.35.8.80:9300");
        ZhugeSDK.getInstance().setupCodelessGetEventsUrl("http://39.98.119.103");
        ZhugeSDK.getInstance().setUploadURL("http://39.98.119.103","");
        ZhugeSDK.getInstance().initWithParam(context,params);

        ZhugeSDK.getInstance().init(context,"111","",null);

    }

    public static void setupMixpanel(Context context) {
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, "c62fcb8fe8649d80bc0fdebe14bb0929");
//        mixpanel.setEnableLogging(true);
    }

    public static void identify(Context context, String cuid, JSONObject pro) {
        ZhugeSDK.getInstance().identify(context,cuid,pro);
    }

    public static void track(Context context, String eventName, JSONObject pro) {
//        ZhugeSDK.getInstance().track(context, eventName, pro);
    }
}
