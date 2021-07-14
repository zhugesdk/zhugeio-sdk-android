package com.zhuge.analysis.stat;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.zhuge.analysis.metrics.Tweaks;
import com.zhuge.analysis.stat.exp.ViewExposeAppState;
import com.zhuge.analysis.stat.exp.entities.ViewExposeData;
import com.zhuge.analysis.stat.exp.utils.ThreadUtil;
import com.zhuge.analysis.util.Utils;
import com.zhuge.analysis.util.ZGLogger;

import com.zhuge.analysis.deepshare.DeepShare;
import com.zhuge.analysis.listeners.ZhugeInAppDataListener;
import com.zhuge.analysis.viewSpider.TrackingDebug;
import com.zhuge.analysis.viewSpider.UpdatesFromZhuge;
import com.zhuge.analysis.viewSpider.ViewCrawler;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;


/**
 * ZhugeSDK开放接口，调用{@code ZhugeSDK.getInstance()}获得实例，调用{@code init()}
 * 进行初始化。
 * Created by Omen on 16/7/1.
 */
@SuppressWarnings("unused")
public class ZhugeSDK implements ZhugeInAppDataListener {

    private static String TAG = "ZhugeSDK";
    private boolean initialized = false;
    private ZGCore  core= null;
    private ZGAppInfo appInfo = null;
    private boolean enableException = false;
    private boolean enableAutoTrack = false;
    private boolean enableCaheDataEncode = false;
    private boolean enableWebViewTrack = false;
    private boolean initDeepShare = false;

    /**
     * 是否自动进行 H5 打通
     */
    public boolean mEnableJavaScriptBridge = false;

    /**
     * 是否自动进行统计页面时长
     */
    public boolean mEnabelDurationOnPage = false;

    public String url = "";
    public String ref = "";

    public String lng = "";
    public String lat = "";

    public String codelessUrl = "";
    public String codelessGetEventsUrl = "";

    private boolean enableVisual = false;
    public boolean enableVisualDebug = false;
    /**
     * 是否启用曝光统计
     */
    private boolean enableViewExpose = false;

    private UpdatesFromZhuge viewCrawler;
    private TrackingDebug trackingDebug;

    private ZhugeInAppDataListener mListener;

    private ViewExposeServer viewExposeServer;

    private static class SingletonHolder {
        private static final ZhugeSDK instance = new ZhugeSDK();
    }

    @Override
    public void zgOnFailed(String reason) {
        if (mListener != null){
            mListener.zgOnFailed(reason);
        }
    }

    @Override
    public void zgOnInAppDataReturned(JSONObject initParams) {
        appInfo.setDeepPram(initParams);
        if (mListener != null){
            mListener.zgOnInAppDataReturned(initParams);
        }
        core.flush();
    }


    public static ZhugeSDK getInstance() {
        return SingletonHolder.instance;
    }

    private ZhugeSDK() {
        appInfo = new ZGAppInfo();
        core = new ZGCore(appInfo);
    }

//    MAX_LOCAL_SIZE
    public void setMaxLocalSize(int size) {
        Constants.MAX_LOCAL_SIZE = size;
    }

//    SEND_SIZE
    public  void setMaxSendSize(int size) {
        Constants.SEND_SIZE = size;
    }

    public void openLog() {
        ZGLogger.openLog();
    }

    public void openDebug() {
        appInfo.debug = true;
    }

    public void openExceptionTrack(){
        this.enableException = true;
    }
    // AutoTrack
    public void cacheDataEncode(){
        this.enableCaheDataEncode = true;
    }
    boolean isEnableCacheDataEnacode(){
        return enableCaheDataEncode;
    }

    // cache
    public void openAutoTrack(){
        this.enableAutoTrack = true;
    }
    public boolean isEnableAutoTrack(){
        return enableAutoTrack;
    }

    /**
     * 开启可视化埋点  必须 init之前 调用
     */
    public void openVisual(){
        this.enableVisual = true;
    }
    boolean isEnableVisual() {
        return enableVisual;
    }

    public void openVisualDebug(){
        this.enableVisualDebug = true;
    }
    public boolean isEnableVisualDebug() {
        return enableVisualDebug;
    }

    public void enableJavaScriptBridge() {
        this.mEnableJavaScriptBridge = true;
    }

    public void enabelDurationOnPage() {
        this.mEnabelDurationOnPage = true;
    }

    /**
     * 启用曝光统计
     */
    public void enableExpTrack() {
        this.enableViewExpose = true;
    }

    // DeepShare
    public void initDeepShare(){
       this.initDeepShare = true;
    }
    boolean isInitDeepShare() {
        return initDeepShare;
    }

    /**
     * 设置输出的日志级别，默认为Log.INFO 4。
     * @param level 日志级别，2 - 6，对应verbose - error。不在这个范围内的数字将按边界处理。
     */
    public void setLogLevel(int level){
        ZGLogger.setLogLevel(level);
    }

    /**
     * 自定义utm信息
     * 参数为包含utm信息的JSONObject,其中key 为
     * utm_source,
     * utm_medium,
     * utm_campaign,
     * utm_content,
     * utm_term
     * @param utm 所要设置的utm信息
     */
    public void setUtm(JSONObject utm){
        this.appInfo.setDeepPram(utm);
    }

    /**
     * 设置 Websocket 地址
     * @param url 默认情况下为 Zhugeio SAAS 地址， 私有化部署需手动配置
     */
    public void setupCodelessUrl(String url){
        this.codelessUrl = url;
    }


    /**
     * 设置获取远程事件 URL 地址
     * @param url 默认情况下为 Zhugeio SAAS 地址， 私有化部署需手动配置
     */
    public void setupCodelessGetEventsUrl(String url){
        this.codelessGetEventsUrl = url;
    }

    /**
     * 设置数据上传地址
     * @param url 默认情况下，数据上传的地址，不可为null
     * @param backupUrl 当默认地址上传失败时的备用地址，如果没有备用地址，可以设置为null
     */
    public void setUploadURL(String url ,String backupUrl){
        ZGLogger.logMessage(TAG,"设置数据上传主地址为："+url+" , 备份地址: "+backupUrl);
        if (null == url || url.length() == 0){
            ZGLogger.logError(TAG,"主上传地址url不合法，请检查输入："+url);
            return;
        }

        String apiPath = Utils.parseUrl(url,Constants.PATH_ENDPOINT,Constants.PATH_ENDPOINT);
        String apiPathBack = Utils.parseUrl(backupUrl,Constants.PATH_ENDPOINT,Constants.PATH_ENDPOINT);
        appInfo.apiPath = apiPath;
        appInfo.apiPathBack = apiPathBack;

        appInfo.ZGSeeUrl = Utils.parseUrl(url,"","");
        appInfo.ZGSeePolicyUrl = Utils.parseUrl(url,"","");

    }

    public void setZGSeeEnable(boolean enable){
        appInfo.setUserDefinedEnableZGSee(enable);
    }

    public void initWithParam(Context context ,ZhugeParam param){
        ZGLogger.logVerbose("自定义配置："+param.toString());
        appInfo.mContent = context.getApplicationContext();
        if (param.did!=null && param.did.length() > 256){
            ZGLogger.logError(TAG,"传入的did过长，SDK停止初始化。请检查did");
            return;
        }


        if (appInfo.did == null && param.did !=null){
            appInfo.did = param.did;
        }

        if (param.appKey!=null && param.appChannel!=null){
            init(context,param.appKey,param.appChannel,null);

            if (this.enableVisual == true) {
                CodeLessConfig.openGestureBindingUI();
                viewCrawler = constructUpdatesFromZhuge(context);
                if (null == viewCrawler) {
                    return;
                }
                viewCrawler.startUpdates();
                trackingDebug = constructTrackingDebug();
            }

        }else {
            ZGLogger.logError(TAG,"未设置APPKEY 和 ZHUGE_CHANNEL，Zhuge将无法统计数据。");
        }

    }

    public void initWithDeepShareAndParam(Activity activity, ZhugeParam param){
        initDeepShare();
        appInfo.mContent = activity.getApplicationContext();
        if (param.did != null && param.did.length() > 256){
            ZGLogger.logError(TAG,"传入的did过长，SDK停止初始化。请检查did :"+param.did);
            return;
        }
        if (appInfo.did == null && param.did !=null){
            appInfo.did = param.did;
        }
        if (param.appKey!=null && param.appChannel!=null){
            init(activity,param.appKey,param.appChannel,param.listener);
        }else {
            ZGLogger.logError(TAG,"未设置APPKEY 和 ZHUGE_CHANNEL，Zhuge将无法统计数据。");
        }
    }

//    @Deprecated
    public void init(Context context,String appKey,String appChannel, ZhugeInAppDataListener userListener){
        if(context instanceof Activity && userListener != null) {
            DeepShare.init((Activity) context,appKey,this);
        }
        if (initialized){
            return;
        }
        if (!appInfo.setAppKey(appKey) || !appInfo.setAppChannel(appChannel)){
            ZGLogger.logError(TAG,"appKey"+appKey+"或appChannel"+appChannel+"无效！");
            return;
        }

        if (appInfo.apiPath != null) {
            String seePath = appInfo.ZGSeeUrl + "sdk_zgsee";
            String seePolicy = appInfo.ZGSeePolicyUrl + "appkey/"+ appInfo.getAppKey();

            appInfo.ZGSeeUrl = seePath;
            appInfo.ZGSeePolicyUrl = seePolicy;
        }

        initialized = true;
        Context mContext = context.getApplicationContext();
        Constants.loadConfig(mContext);
        core.init(mContext);//内部初始化，包括缓存文件，应用信息，设备标识信息
        if (enableException){
            CrashHandler.getInstance().init(core);
        }
        boolean featureEnable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        if (appInfo.isInMainThread && featureEnable){
            Application application = null;
            if (mContext instanceof  Application){
                application = (Application) mContext;
                application.registerActivityLifecycleCallbacks(new ZhugeCallbacks(core));
            }
            if (enableViewExpose){
                ViewExposeAppState appState = new ViewExposeAppState();
                if (application != null){
                    application.registerActivityLifecycleCallbacks(appState);
                }
                viewExposeServer = new ViewExposeServer(appState);
            }
        }
    }

    /**
     * 开始一个时长追踪事件，这个接口并不会真正的产生事件，当你调用{@link #endTrack(String, JSONObject)}时，会在事件
     * 中添加一个duration属性，标记这个事件从调用这个接口开始所经过的时间，以秒为单位。
     * @param event_name 事件名称
     */
    public void startTrack(String event_name){
        core.sendObjMessage(Constants.MESSAGE_START_TRACK,event_name);
    }

    /**
     *结束时长追踪，如果同样的event_name之前并没有调用{@link #startTrack(String)}，那么这个方法会立即返回。
     * @param event_name 事件名称，需在之前调用过startTrack
     * @param properties 事件属性，可以为空。
     */
    public void endTrack(final String event_name,final JSONObject properties){
        JSONObject copy = Utils.cloneJSONObject(properties);
        core.sendEventMessage(Constants.MESSAGE_END_TRACK,event_name,copy);
    }

    public void autoTrackEvent(JSONObject properties) {
        JSONObject copy = Utils.cloneJSONObject(properties);
        core.sendObjMessage(Constants.MESSAGE_AUTO_TRACK,copy);
    }

    public void trackDurationOnPage(JSONObject properties) {
        JSONObject copy = Utils.cloneJSONObject(properties);
        core.sendObjMessage(Constants.MESSAGE_DURATION_EVENT,copy);
    }


    public void trackRevenue(Context context, HashMap<String, Object> pro) {
        if (pro == null){
            ZGLogger.logError(TAG,"购买事件属性不能为空");
            return;
        }
        JSONObject object = new JSONObject(pro);
        trackRevenue(context,object);
    }

    public void trackRevenue(Context context, JSONObject jsonObject) {

        try {

            BigDecimal price = new BigDecimal(jsonObject.getString(Constants.ZhugeEventRevenuePrice));
            BigDecimal number = new BigDecimal(jsonObject.getString(Constants.ZhugeEventRevenueProductQuantity));
            BigDecimal total = price.multiply(number);

            jsonObject.put(Constants.ZhugeEventRevenuePrice,price);
            jsonObject.put(Constants.ZhugeEventRevenueProductQuantity,number);
            jsonObject.put(Constants.ZhugeEventRevenueTotalPrice, total);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject copy = Utils.conversionRevenuePropertiesKey(jsonObject);
        core.sendEventMessage(Constants.MESSAGE_REVENUE_EVENT,"revenue",copy);
    }

    public void track(Context context,String eventName){
        if (eventName == null || eventName.length() == 0){
            ZGLogger.logError("ZhugeSDK","自定义事件的事件名称传入空值是错误的。");
            return;
        }
        core.sendEventMessage(Constants.MESSAGE_CUSTOM_EVENT,eventName,null);
    }

    public void track(Context context, String eventName , JSONObject jsonObject){
        if (eventName == null || eventName.length() == 0){
            ZGLogger.logError("ZhugeSDK","自定义事件的事件名称传入空值是错误的。");
            return;
        }
        JSONObject copy = Utils.cloneJSONObject(jsonObject);
        core.sendEventMessage(Constants.MESSAGE_CUSTOM_EVENT,eventName,copy);
    }

    public void track(Context context, String eventName, HashMap<String,Object> pro){
        if (pro == null){
            ZGLogger.logError(TAG,"自定义事件属性不能为空, 事件 :"+eventName+"被丢弃");
            return;
        }
        JSONObject object = new JSONObject(pro);
        track(context,eventName,object);
    }

    /**
     * view曝光统计，必须在主线程调用
     * 使用{@link com.zhuge.analysis.stat.exp.entities.ViewExposeData} 进行事件封装
     *
     * @param viewExp 曝光统计独享
     */
    public void viewExpTrack(ViewExposeData viewExp) {
        if (!enableViewExpose) {
            ZGLogger.logError(TAG, "未启用曝光统计，请调用ZhugeSDK.getInstance().enableExpTrack()启用曝光统计");
            return;
        }
        if (!ThreadUtil.isRunningOnUiThread()) {
            ZGLogger.logError(TAG, "view曝光统计必须在主线程运行！");
            return;
        }
        if (viewExp == null) {
            ZGLogger.logError(TAG, "view曝光统计参数不能为空！");
            return;
        }
        if (TextUtils.isEmpty(viewExp.getEventName())) {
            ZGLogger.logError(TAG, "view曝光统计必须包含可用的事件名称!");
            return;
        }
        if (viewExp.getView() == null) {
            ZGLogger.logError(TAG, "view曝光统计所传递view不能为null！");
            return;
        }
        if (null != viewExposeServer) {
            viewExposeServer.markViewExp(viewExp);
        }
    }

    public void identify(Context context,String uid,JSONObject object){
        if (uid == null || uid.length() == 0){
            ZGLogger.logError("ZhugeSDK","标识用户传入空的uid是错误的。");
            return;
        }
        JSONObject copy = Utils.cloneJSONObject(object);
        core.sendEventMessage(Constants.MESSAGE_IDENTIFY_USER,uid,copy);
    }

    public void identify(Context context, String uid, HashMap<String, Object> kv) {
        identify(context, uid, new JSONObject(kv));
    }

    public void flush(Context applicationContext) {
        core.flush();
    }


    public void setThirdPartyPushUserId(PushChannel channel, String userId) {
        if (null == channel || null == userId || userId.length()<5) {
            return;
        }
        if (!initialized){
            ZGLogger.logError(TAG,"调用setThirdPartyPushUserId之前，请先调用init。");
            return;
        }
        JSONObject JSONObject = appInfo.channelData(channel.toString(), userId);
        core.sendObjMessage(Constants.MESSAGE_NEED_SEND,JSONObject);
    }

    public void onMsgReaded(PushChannel channel, Object t) {
        if (!initialized){
            ZGLogger.logError(TAG,"调用onMsgReaded之前，请先调用init。");
            return;
        }
        JSONObject info = appInfo.parseMid(ZGAppInfo.MSG_READ, channel, t);
        if (null != info){
            core.sendObjMessage(Constants.MESSAGE_NEED_SEND,info);
        }
    }

    public void onMsgRecved(PushChannel channel, Object t) {
        if (!initialized){
            ZGLogger.logError(TAG,"调用onMsgReaded之前，请先调用init。");
            return;
        }
        JSONObject JSONObject = appInfo.parseMid(ZGAppInfo.MSG_RECV, channel, t);
        if (null != JSONObject){
            core.sendObjMessage(Constants.MESSAGE_NEED_SEND,JSONObject);
        }

    }

    public void setPlatform(JSONObject object){
        if (!initialized || null == object){
            ZGLogger.logError(TAG,"未初始化，请先调用init。");
            return;
        }
        JSONObject clone = Utils.cloneJSONObject(object);
        core.sendObjMessage(Constants.MESSAGE_SET_DEVICE_INFO,clone);
    }

    public void setSuperProperty(JSONObject object){
        if (!initialized || null == object){
            ZGLogger.logError(TAG,"未初始化，请先调用init。");
            return;
        }
        JSONObject clone = Utils.cloneJSONObject(object);
        core.sendObjMessage(Constants.MESSAGE_SET_EVENT_INFO,clone);
    }

    public String getDid(){
        return appInfo.getDid();
    }

    public long getSid(){
        return appInfo.getSessionID();
    }

    private TrackingDebug constructTrackingDebug() {
        if (viewCrawler instanceof ViewCrawler) {
            return (TrackingDebug) viewCrawler;
        }
        return null;
    }

    private UpdatesFromZhuge constructUpdatesFromZhuge(Context mContext) {
        if (Build.VERSION.SDK_INT < CodeLessConfig.UI_FEATURES_MIN_API) {
            ZGLogger.logVerbose("当前的安卓版本不支持动态打点");
            return null;
        } else {
            if (null != viewCrawler){
                return viewCrawler;
            }
            return new ViewCrawler(mContext, appInfo.getAppKey(), appInfo.appVersion, new Tweaks());
        }
    }


    public enum PushChannel {
        JPUSH("jpush"), UMENG("umeng"), XIAOMI("xiaomi"), BAIDU("baidu"), XINGE("xinge"), GETUI("getui");

        private String value;

        PushChannel(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static class ZhugeJS{
        public ZhugeJS(){
        }

        @JavascriptInterface
        public void trackProperty(String event , String pro){
            ZGLogger.logVerbose("调用JS接口，"+event +"属性："+pro);
            try {
                JSONObject object = new JSONObject(pro);
                ZhugeSDK.getInstance().track(null,event,object);
            } catch (JSONException e) {
                Log.e("Zhuge","传入的json String有误。："+pro);
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void identifyProperty(String uid ,String pro){
            ZGLogger.logVerbose("调用JS接口，标记用户"+uid +"属性 ："+pro);
            try {
                JSONObject object = new JSONObject(pro);
                ZhugeSDK.getInstance().identify(null,uid,object);
            } catch (JSONException e) {
                Log.e("Zhuge","传入的json String有误。："+pro);
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void autoTrackProperty(String type,String pro){
            ZGLogger.logVerbose("autoTrackProperty，"+type+ "属性 ："+pro);
            try {
                JSONObject object = new JSONObject(pro);
                object.put("$eid",type);
                ZhugeSDK.getInstance().core.sendObjMessage(Constants.MESSAGE_AUTO_TRACK,object);
            } catch (JSONException e) {
                ZGLogger.logError("Zhuge","传入的json String有误。："+pro);
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void trackDurationOnPage (String eid, String pro) {
            ZGLogger.logVerbose("TrackDurationProperty，"+eid+ "属性 ："+pro);
            try {
                JSONObject object = new JSONObject(pro);
                object.put("$eid",eid);
                ZhugeSDK.getInstance().core.sendObjMessage(Constants.MESSAGE_DURATION_EVENT,object);
            } catch (JSONException e) {
                ZGLogger.logError("Zhuge","传入的json String有误。："+pro);
                e.printStackTrace();
            }
        }
    }
}
