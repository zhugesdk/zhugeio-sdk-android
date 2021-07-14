package com.zhuge.analysis.stat;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.zhuge.analysis.util.HttpServices;
import com.zhuge.analysis.util.Utils;
import com.zhuge.analysis.util.ZGJSONObject;
import com.zhuge.analysis.util.ZGLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 逻辑处理，事件分发
 * Created by Omen on 16/9/9.
 */
/*package*/ class ZGCore {

    private ZGWorker  zgWorker;
    private ZGHttpWorker  zgHttpWorker;
    private Context   mContext;
    ZGAppInfo appInfo;
    private final Map<String, Long> mEventTimings = new HashMap<>();
    private static final String TAG = "com.zhuge.ZGCore";
    private int foregroundActivities = 0;

//    private boolean FlushGrant = false;

    private boolean real = false;
    private int permissionNet = 4;

     String pageName;
    /*package*/ ZGCore(ZGAppInfo appInfo){
        this.appInfo = appInfo;
        HandlerThread workerThread =  new HandlerThread("com.zhuge.worker");
        workerThread.start();
        zgWorker = new ZGWorker(workerThread.getLooper());

        HandlerThread httpThread =  new HandlerThread("com.zhuge.http");
        httpThread.start();
        zgHttpWorker = new ZGHttpWorker(httpThread.getLooper());

    }
    /**
     * 初始化设备信息，用户自定义字段
     * @param context 上下文
     */
    /*package*/ void init(Context context) {
        mContext = context.getApplicationContext();
        appInfo.initGlobalSettingFile(mContext);//初始化配置文件
        appInfo.initAppInfo(mContext);//初始化应用信息
        appInfo.upgradeSharedPrefs(mContext); //查看是否需要更新
        appInfo.initDeviceInfo(mContext);//上传设备id
        if (appInfo.did == null){
            ZGLogger.logError(TAG,"did生成失败。");
        }
        appInfo.logInitInfo();//输出初始化信息
        zgWorker.sendEmptyMessage(Constants.MESSAGE_DEVICE_INFO);//发送设备信息

        if (appInfo.isZGSeeEnable()) {
            zgHttpWorker.sendEmptyMessage(Constants.MESSAGE_CHECK_APP_SEE);
        }


    }

    /*package*/ void onEnterForeground(String name) {
        foregroundActivities++;
        Message message = zgWorker.obtainMessage(Constants.MESSAGE_CHECK_SESSION,name);
        message.sendToTarget();
    }

    /*package*/ void onExitForeground(String localClassName) {
        foregroundActivities --;
        Message message = zgWorker.obtainMessage(Constants.MESSAGE_UPDATE_SESSION);
        message.obj = localClassName;
        message.sendToTarget();
    }

    /**
     * 发送事件消息
     * @param messageWhat 标识数据
     * @param eventName 事件名称，或者cuid
     * @param pro 事件属性
     */
    /*package*/ void sendEventMessage(int messageWhat,String eventName,JSONObject pro){
        EventDescription eventDescription = new EventDescription(eventName,pro);
        Message message = zgWorker.obtainMessage(messageWhat);
        message.obj = eventDescription;
        message.sendToTarget();
    }

    /**
     * 发送单个数据消息
     * @param messageWhat 标识数据
     * @param object 要发送的数据
     */
    /*package*/ void sendObjMessage(int messageWhat,Object object) {
        Message message = zgWorker.obtainMessage(messageWhat);
        message.obj = object;
        message.sendToTarget();
    }

    /*package*/ void flush() {
        Message message = zgWorker.obtainMessage(Constants.MESSAGE_FLUSH);
        message.sendToTarget();
//        if (!FlushGrant) {
//            FlushGrant = grant;
//        }
    }

    /**
     * 发送屏幕截图
     * @param info 截图信息
     */
    /*package*/ void sendScreenshot(ScreenshotInfo info) {
        boolean b = appInfo.updateZGSeeState();
        if (b){
            Message message = zgWorker.obtainMessage(Constants.MESSAGE_DEVICE_INFO,1,0);
            message.sendToTarget();
        }
        Message message = zgWorker.obtainMessage(Constants.MESSAGE_SEND_SCREENSHOT);
        message.obj = info;
        message.sendToTarget();
    }

    public void setScreenSize(int width, int height) {
        JSONArray array = new JSONArray();
        array.put(width);
        array.put(height);
        appInfo.screenSize = array;
    }

    public void onException(Thread thread,Throwable ex) {
        boolean isForgound = foregroundActivities>0;
        JSONObject object = appInfo.buildException(thread, ex, isForgound);
        sendObjMessage(Constants.MESSAGE_NEED_SEND,object);
    }

    private static class EventDescription {
        EventDescription(String eventName, JSONObject properties) {
            this.eventName = eventName;
            this.properties = properties;
        }
        private String eventName;
        private JSONObject properties;
    }

    static class ScreenshotInfo {
        /**
         * 坐标点
         */
        JSONArray points;
        /**
         * 当前页面名称
         */
        String    pageName;
        /**
         * 当前页面停留时长
         */
        long      pageStayTime;
        /**
         * 动作持续时长（滑动了多久）
         */
        long      actionTime;

        /**
         * base64编码的屏幕截图
         */
        String    screenshot;
        /**
         * 事件名称
         */
        String    eid;
        /**
         * 页面路径，使用页面的完整类名
         */
        String pageUrl;
        /**
         *操作间隔时长
         */
        double  gap;

        /**
         * 打码区域
         */
        JSONArray mosaicViewArray;
        ScreenshotInfo(){
        }

        void setPoints(JSONArray points) {
            this.points = points;
        }

        void setPageName(String pageName) {
            this.pageName = pageName;
        }

        void setPageStayTime(long pageStayTime) {
            this.pageStayTime = pageStayTime;
        }

        void setActionTime(long actionTime) {
            this.actionTime = actionTime;
        }


        void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        void setEid(String eid) {
            this.eid = eid;
        }

        void setGap(double gap) {
            this.gap = gap;
        }
        void setPageUrl(String pageUrl){
            this.pageUrl = pageUrl;
        }
        void setMosaicViewArray(JSONArray array){
            this.mosaicViewArray = array;
        }
    }


    private class ZGWorker extends Handler{
        private ZhugeDbAdapter dbAdapter;
        private HttpServices httpService;
        private boolean sdkProcessing = false;
        private boolean appSeeProcessing = false;
        private long localEventSize = 0;
        private long today_send_count = 0;
        public ZGWorker(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mContext == null){
                ZGLogger.logError(TAG,"未正确初始化，请在应用入口调用ZhugeSDK.getInstance().init();");
                return;
            }
            if (dbAdapter == null){
                dbAdapter = new ZhugeDbAdapter(mContext);
                //初始化本地缓存事件数
                localEventSize = dbAdapter.getEventCount();
                if (appInfo.getGlobalSP() != null) {
                    String todayInfo = appInfo.getGlobalSP().getString(Constants.SP_TODAY_COUNT, "");
                    if (!"".equals(todayInfo)) {
                        String[] toInfo = todayInfo.split("\\|");
                        if ((System.currentTimeMillis() / 1000 / 86400 - Integer.parseInt(toInfo[0])) != 0) {
                            today_send_count = 0;
                        } else {
                            today_send_count = Integer.parseInt(toInfo[1]);
                        }
                    }
                } else {
                    ZGLogger.logVerbose("SDK 没有正常初始化");
                }

            }
            int stateCode = -1;
            switch (msg.what){

                case Constants.MESSAGE_DEVICE_INFO:

                    JSONObject deviceInfo = appInfo.buildDeviceInfo(mContext,msg.arg1 == 1);
                    if (deviceInfo == null){
                        break;
                    }
                    addEvent(deviceInfo);
                    updateDeviceInfoTime();
                    stateCode = Constants.CODE_NEED_FLUSH;
                    break;
                case Constants.MESSAGE_CHECK_SESSION:
                    String sessionName = msg.obj.toString();
                    stateCode = startNewSessionIfNeed(sessionName);
                    break;
                case Constants.MESSAGE_CUSTOM_EVENT:
                    EventDescription description = (EventDescription) msg.obj;
                    String name = description.eventName;
                    JSONObject pro = description.properties;
                    if (appInfo.sessionID<0 && appInfo.isInMainThread){
                        startNewSessionIfNeed("et_in_main_thread");
                    }
                    JSONObject info = appInfo.buildCustomEvent(name,pro);
                    updateSessionActivity("自定义事件更新会话");
                    stateCode = addEvent(info);
                    break;

                case Constants.MESSAGE_REVENUE_EVENT:
                    EventDescription revenueDes = (EventDescription) msg.obj;
                    String revenueName = revenueDes.eventName;
                    JSONObject revenuePro = revenueDes.properties;
                    JSONObject revenueInfo = appInfo.buildRevenueEvent(revenueName,revenuePro);
                    updateSessionActivity("收入事件更新会话");
                    stateCode = addEvent(revenueInfo);
                    break;
                case Constants.MESSAGE_IDENTIFY_USER:
                    EventDescription userDes = (EventDescription) msg.obj;
                    String uid = userDes.eventName;
                    JSONObject uPro = userDes.properties;
                    JSONObject userPro = appInfo.buildIdentify(uid,uPro);
                    updateCUID(uid);
                    updateSessionActivity("标记用户更新会话");
                    addEvent(userPro);
                    stateCode = Constants.CODE_NEED_FLUSH;
                    break;
                case Constants.MESSAGE_FLUSH:
                    boolean check = checkNetwork(true);
                    if (check && !sdkProcessing){
                        ZGLogger.logMessage(TAG," flush event");
                        if (ZhugeSDK.getInstance().isInitDeepShare()) {
                            flushEventWithDeepShare();
                        } else {
                            flushEvent();
                        }
                    }
                    break;

                case Constants.MESSAGE_NEED_SEND:
                    JSONObject data = (JSONObject) msg.obj;
                    addEvent(data);
                    if (ZhugeSDK.getInstance().isInitDeepShare()) {
                        flushEventWithDeepShare();
                    } else {
                        flushEvent();
                    }
                    break;
                case Constants.MESSAGE_UPDATE_SESSION:
                    String page = (String) msg.obj;
                    appInfo.getGlobalSP().edit().putString(Constants.SP_LAST_PAGE,page).apply();
                    updateSessionActivity("退出前台，更新会话时间");

                    break;
                case Constants.MESSAGE_START_TRACK:
                    String event = (String) msg.obj;
                    mEventTimings.put(event,System.currentTimeMillis());
                    break;
                case Constants.MESSAGE_END_TRACK:
                    EventDescription obj = (EventDescription) msg.obj;
                    if (appInfo.sessionID<0 && appInfo.isInMainThread){
                        startNewSessionIfNeed("et_in_main_thread");
                    }
                    stateCode = endTrack(obj);
                    break;
                case Constants.MESSAGE_SET_EVENT_INFO:
                    JSONObject set = (JSONObject) msg.obj;
                    ZGLogger.logVerbose("设置事件全局信息:"+set.toString());
                    appInfo.getGlobalSP().edit().putString(Constants.SP_USER_DEFINE_EVENT,set.toString()).apply();

                    break;
                case Constants.MESSAGE_SET_DEVICE_INFO:
                    JSONObject device = (JSONObject) msg.obj;
                    ZGLogger.logVerbose("设置自定义设备环境:"+device.toString());
                    appInfo.getGlobalSP().edit().putString(Constants.SP_USER_DEFINE_DEVICE,device.toString()).apply();

                    break;
                case Constants.MESSAGE_SEND_SCREENSHOT:
                    ScreenshotInfo eventDescription = (ScreenshotInfo) msg.obj;
                    JSONObject screenshot = appInfo.buildScreenshot(eventDescription);
                    addScreenshot(screenshot);
                    break;
                case Constants.MESSAGE_ZGSEE_UPLOAD_OK: //本地数据上传返回信息
                    appSeeProcessing = false;
                    if (msg.arg1 >0){ //代表发送成功
                        dbAdapter.removeEventFromSee(Integer.toString(msg.arg1));
                    }else if (msg.arg1 < 0){
                        break;
                    }
                case Constants.MESSAGE_ZGSEE_CHECK_LOCAL:
                    if (!appSeeProcessing){
                        checkAppSeeLocal();
                    }
                    break;
                case Constants.MESSAGE_SDK_UPLOAD_OK:
                    sdkProcessing = false;
                    int lastID = msg.arg1;
                    int length = msg.arg2;
                    if (lastID!=0 && length!=0){
                        today_send_count += length;
                        localEventSize -= length;
                        dbAdapter.removeEvent(Integer.toString(lastID));
                        updateTodayCount();
                        ZGLogger.logMessage(TAG,"发送成功，今日已发送"+today_send_count+"条数据。");
                        if (appInfo.debug){
                         stateCode = Constants.CODE_NEED_FLUSH;
                        }else {
                            stateCode = localEventSize == 0? -1: (int) localEventSize;
                        }
                    }else {
                        //发送失败，重新发送
                        stateCode = Constants.CODE_NEED_FLUSH;
                    }
                    break;
                case Constants.MESSAGE_AUTO_TRACK:
                    JSONObject clickPro = (JSONObject) msg.obj;
                    JSONObject autoEvent = appInfo.buildAutoTrackEvent(clickPro);
                    if (autoEvent!=null){
                        stateCode=addEvent(autoEvent);
                    }
                    break;

                case Constants.MESSAGE_DURATION_EVENT:

                    JSONObject durPro = (JSONObject) msg.obj;
                    JSONObject durEvent = appInfo.buildDurTrackEvent(durPro);
                    if (durEvent!=null){
                        stateCode=addEvent(durEvent);
                    }
                    break;

                case Constants.MESSAGE_CHECK_APP_SEE_RETURN:
                    JSONObject checkReturn = (JSONObject) msg.obj;
                    if (checkReturn == null){
                        break;
                    }
//                    ZGLogger.logMessage(TAG,"check zgsee return "+checkReturn.toString());
                    int policy = checkReturn.optInt("policy", -1);
                    int realNum = checkReturn.optInt("real", -1);
                    permissionNet = checkReturn.optInt("net", -1);
                    Utils.publicKey = checkReturn.optString("rsa-public", null);
                    appInfo.setServerPolicy(policy);
                    appInfo.setRSAMd5(checkReturn.optString("rsa-md5",""));
                    appInfo.setMosaic(checkReturn.optInt("mosaic"));
                    real = realNum == 0;
                    ZGLogger.logMessage(TAG,"zhuge see config."+checkReturn);
                    if (appInfo.isZGSeeEnable()){
                        sendEmptyMessage(Constants.MESSAGE_ZGSEE_CHECK_LOCAL);
                    }
                    break;
            }
            if ((stateCode == Constants.CODE_NEED_FLUSH || stateCode >=Constants.UPLOAD_LIMIT_SIZE) && !hasMessages(Constants.MESSAGE_FLUSH)){
                sendEmptyMessageDelayed(Constants.MESSAGE_FLUSH,Constants.FLUSH_INTERVAL);
            }
        }

        private void checkAppSeeLocal() {
            int networkType = appInfo.connectivityUtils.getNetworkType();
            if (permissionNet != 1 && networkType != permissionNet){
                ZGLogger.logMessage(TAG,"check Local return, cause  net is  "+permissionNet +" , and local net is "+networkType);
                return;
            }
            ZGLogger.logMessage(TAG,"check Local start");
            Pair<String, JSONArray> dataFromSee = dbAdapter.getDataFromSee(appInfo.sessionID);
            if (dataFromSee == null){
                return;
            }
            JSONObject jsonObject = appInfo.wrapDataWithArray(dataFromSee.second);
            Message message = zgHttpWorker.obtainMessage(ZGHttpWorker.MESSAGE_UPLOAD_ZGSEE);
            message.obj = jsonObject;
            message.arg1 = Integer.parseInt(dataFromSee.first);
            ZGLogger.logMessage(TAG,"start upload app "+dataFromSee.first);
            message.sendToTarget();
            appSeeProcessing = true;
        }

        private boolean checkNetwork(boolean repeat){
            if (!appInfo.connectivityUtils.isOnline()){
                ZGLogger.logMessage(TAG,"网络不可用，暂停发送。");
                if (repeat){
                    sendEmptyMessageDelayed(Constants.MESSAGE_FLUSH,60*1000);//30秒后重试
                }
                return false;
            }
            return true;
        }

        private void addScreenshot(JSONObject screenshot) {
            if (screenshot == null)
                return;
            try {
                //实时上传，并且网络环境允许
                if (appInfo.isZGSeeEnable() && real && (permissionNet == 1 || appInfo.connectivityUtils.getNetworkType() == permissionNet)){
                    JSONArray array = new JSONArray();
                    array.put(screenshot);
                    JSONObject jsonObject = appInfo.wrapDataWithArray(array);
                    Message message = zgHttpWorker.obtainMessage(ZGHttpWorker.MESSAGE_UPLOAD_ZGSEE);
                    message.obj = jsonObject;
                    message.arg1 = -1;
                    ZGLogger.logMessage(TAG,"start upload app ");
                    message.sendToTarget();
                }else {//非实时，或者网络环境不允许，本地缓存
                    int i = dbAdapter.addEventToSee(appInfo.isZGSeeEnable(), screenshot, appInfo.sessionID);
                    if (i >= 60){
                        sendEmptyMessage(Constants.MESSAGE_ZGSEE_CHECK_LOCAL);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void updateDeviceInfoTime() {
            appInfo.getGlobalSP().edit().putLong(Constants.SP_UPDATE_DEVICE_TIME,System.currentTimeMillis()).apply();

        }

        private void updateCUID(String uid) {
//            String encodeUid = Base64.encodeToString(uid.getBytes(),Base64.DEFAULT);
            appInfo.getGlobalSP().edit().putString(Constants.SP_CUID,uid).apply();

        }



        private int endTrack(EventDescription description) {
            String event_name = description.eventName;
            JSONObject properties = description.properties;
            Long begin = mEventTimings.get(event_name);
            if (null == begin)
                return -1;
            mEventTimings.remove(event_name);
            long now = System.currentTimeMillis();
            long dru = now - begin;
            try {
                if (properties == null){
                    JSONObject obj = new JSONObject();
                    obj.put("$dru",dru);
                    JSONObject jsonObject = appInfo.buildCustomEvent(event_name, obj);
                    return addEvent(jsonObject);
                }else {
                    properties.put("$dru",dru);
                    JSONObject jsonObject = appInfo.buildCustomEvent(event_name,properties);
                    return addEvent(jsonObject);
                }
            }catch (Exception e){
                ZGLogger.handleException(TAG,"时长追踪事件错误",e);
                return -1;
            }
        }

        private int startNewSessionIfNeed(String name) {
            long timestamp = System.currentTimeMillis();
            if (appInfo.sessionID>0){
                if (timestamp - appInfo.lastSessionActivityTime < Constants.SESSION_EXCEED){
                    updateSessionActivity("session ID>0");
                    ZGLogger.logVerbose("已经初始化，更新会话时间");
                    return -1;
                }
                startSession(name);
                ZGLogger.logVerbose("已经初始化，开始新的会话");
                return  Constants.CODE_NEED_FLUSH;
            }else {
                String lastSession = "";
                lastSession = appInfo.getGlobalSP().getString(Constants.SP_LAST_SESSION_TIME, "");
                if (!lastSession.equals("")){
                    String[] last = lastSession.split("\\|");
                    long session = Long.parseLong(last[0]);
                    long lastTime = Long.parseLong(last[1]);
                    if (session <=0 ||timestamp - lastTime >Constants.SESSION_EXCEED){
                        ZGLogger.logVerbose("第一次进入，距离上次超时，开始新的会话");
                        startSession(name);
                        return Constants.CODE_NEED_FLUSH;
                    }else {
                        //继承上次的会话ID
                        ZGLogger.logVerbose("第一次进入，继承上次会话");
                        appInfo.sessionID = session;
                        updateSessionActivity("继承上次会话");
                        return -1;
                    }
                }else {
                    ZGLogger.logVerbose("第一次进入，没有上次，开始新的会话");
                    startSession(name);
                    return Constants.CODE_NEED_FLUSH;
                }
            }
        }

        /**
         * 开启新的会话，并在开关打开的情况下，上传appSee数据
         * @param name 开始会话的名称
         */
        private void startSession(String name) {
            appInfo.sessionID = System.currentTimeMillis();
            if (Constants.ENABLE_SESSION_TRACK){
                completeLastSession();
                zgWorker.sendEmptyMessage(Constants.MESSAGE_DEVICE_INFO);//发送设备信息
                JSONObject st = appInfo.buildSessionStart(name);
                if (null == st)
                    return;
                addEvent(st);
                if (appInfo.isZGSeeEnable()){
                    JSONObject object = appInfo.buildSeeStart();
                    addScreenshot(object);
                }
            }
            updateSessionActivity("会话开始，更新会话时间");
        }

        private void completeLastSession() {
            JSONObject se = appInfo.buildSessionEnd();
            if (null == se)
                return;
            addEvent(se);
        }

        private void updateSessionActivity(String s) {
            ZGLogger.logVerbose("updateSessionActivity "+s);
            long now = System.currentTimeMillis();
            appInfo.lastSessionActivityTime = now;
            String updateSession = appInfo.sessionID + "|" + now;
            appInfo.getGlobalSP().edit().putString(Constants.SP_LAST_SESSION_TIME, updateSession).apply();

        }

        private int addEvent(JSONObject event){
            if (localEventSize >= Constants.MAX_LOCAL_SIZE){
                ZGLogger.logError(TAG,"本地存储事件超过最大值，事件将被丢弃。");
                return -1;
            }
            if (event == null){
                return -1;
            }

            ZGLogger.logVerbose("添加事件\n"+event.toString());
            int count =  dbAdapter.addEvent(event);
            localEventSize = count;
            if (appInfo.debug){
                return Constants.UPLOAD_LIMIT_SIZE;//实时调试开启之后，要立即上传事件
            }
            return count;
        }

        private int flushEventWithDeepShare() {
            if (!appInfo.connectivityUtils.isOnline()){
                ZGLogger.logMessage(TAG,"网络不可用，暂停发送。");
                sendEmptyMessageDelayed(Constants.MESSAGE_FLUSH,30*1000);//30秒后重试
                return -1;
            }
            if (today_send_count >= Constants.SEND_SIZE){
                ZGLogger.logMessage(TAG,"当日已达最大上传数，暂停发送事件。");
                return -1;
            }
            if (null == httpService){
                httpService = new HttpServices();
            }
            String[] mDbData = dbAdapter.getDataAttachDeepShare(appInfo.sessionID,appInfo.deepPram);
            if (null == mDbData){
                return -1;
            }
            try {
                final Map<String, Object> postMap = new HashMap<>();
                JSONObject postData = appInfo.wrapDataWithString(mDbData[1]);
                String data = Base64.encodeToString(Utils.compress(postData.toString().getBytes("UTF-8")),
                        Base64.DEFAULT).replace("\r", "").replace("\n", "");
                postMap.put("method", "event_statis_srv.upload");
                postMap.put("compress", "1");
                postMap.put("event", data);
                String url , backUrl;
                if (appInfo.apiPath != null){
                    url = appInfo.apiPath;
                    backUrl = appInfo.apiPathBack;
                }else {
                    url = Constants.API_PATH;
                    backUrl = Constants.API_PATH_BACKUP;
                }
                byte[] returnByte = httpService.requestApi(url, backUrl,postMap);
                if (returnByte == null){
                    ZGLogger.logMessage(TAG,"发送失败，未获得服务端返回数据。");
                    return -1;
                }
                String s = new String(returnByte, "UTF-8");
                JSONObject responseDict = new JSONObject(s);
                int return_code = responseDict.optInt("return_code");
                if (return_code == 0){
                    int count = Integer.parseInt(mDbData[2]);
                    String id = mDbData[0];
                    today_send_count += count;
                    localEventSize -= count;
                    dbAdapter.removeEvent(id);
                    updateTodayCount();
                    ZGLogger.logMessage(TAG,"发送成功，今日已发送"+today_send_count+"条数据。");
                }else {
                    ZGLogger.logMessage(TAG,"发送失败，返回信息："+responseDict.toString());
                }
                return (int) localEventSize;
            } catch (Exception e) {
                ZGLogger.handleException(TAG,"发送数据出错。",e);
                return -1;
            }
        }
        private void flushEvent() {
            if (today_send_count >= Constants.SEND_SIZE){
                ZGLogger.logMessage(TAG,"当日已达最大上传数，暂停发送事件。");
                return ;
            }
            Pair<String,JSONArray> mDbData = dbAdapter.getData();
            if (null == mDbData){
                ZGLogger.logMessage(TAG," flush return , no more sdk data");
                return ;
            }
            try {
                JSONArray array = mDbData.second;
                JSONObject postData = appInfo.wrapDataWithArray(array);

                Message message = zgHttpWorker.obtainMessage(ZGHttpWorker.MESSAGE_UPLOAD_SDK);
                message.obj = postData;
                message.arg1 = Integer.parseInt(mDbData.first);
                message.arg2 = array.length();
                message.sendToTarget();
                ZGLogger.logMessage(TAG,"flush event send sdk data, arg1 : "+mDbData.first);
                sdkProcessing = true;
            } catch (Exception e) {
                ZGLogger.handleException(TAG,"发送数据出错。",e);
            }
        }

        private void updateTodayCount() {
            long day = System.currentTimeMillis()/1000/86400;
            String value = day+"|"+today_send_count;
            appInfo.getGlobalSP().edit().putString(Constants.SP_TODAY_COUNT,value).apply();
        }
    }

    private class ZGHttpWorker extends Handler{
        public static final int MESSAGE_UPLOAD_SDK = 0;
        public static final int MESSAGE_UPLOAD_ZGSEE = 1;
        private static final String HTTP_TAG = "ZGHttp.work";

        private HttpServices httpService;
        public ZGHttpWorker(Looper looper){
            super(looper);
            httpService = new HttpServices();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_UPLOAD_SDK:
                    JSONObject sdkData = (JSONObject) msg.obj;
                    uploadSDKData(sdkData,msg.arg1 , msg.arg2);

                    break;
                case MESSAGE_UPLOAD_ZGSEE:
                    JSONObject data = (JSONObject) msg.obj;
                    uploadZGSee(data,msg.arg1);


                    break;
                case Constants.MESSAGE_CHECK_APP_SEE:
                    checkAppSee();
                    break;
            }
        }

        private void uploadSDKData(JSONObject object, int lastID, int length) {
            boolean success = false;
            try {
                String data = Base64.encodeToString(Utils.compress(object.toString().getBytes()),
                        Base64.DEFAULT).replace("\r", "").replace("\n", "");
                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("method","event_statis_srv.upload");
                builder.appendQueryParameter("compress","1");
                builder.appendQueryParameter("event",data);
                byte[] query = builder.build().getEncodedQuery().getBytes("UTF-8");
                String url ;
                String back;
                if (appInfo.apiPath == null){
                    url = Constants.API_PATH;
                    back = Constants.API_PATH_BACKUP;
                }else {
                    url = appInfo.apiPath;
                    back = appInfo.apiPathBack;
                }
                byte[] returnByte = httpService.sendRequest(url,back,query);
                String s = new String(returnByte, "UTF-8");
                JSONObject responseDict = new JSONObject(s);
                int return_code = responseDict.optInt("return_code");
                if (return_code == 0){
                    success = true;
                    //发送成功，告诉worker本次上传的数量，要删除的数据索引
                }
            }catch (Exception e){
                ZGLogger.handleException(HTTP_TAG,"upload sdk data error",e);
            }finally {
                if (success){
                    zgWorker.obtainMessage(Constants.MESSAGE_SDK_UPLOAD_OK,lastID,length).sendToTarget();
                }else {
                    zgWorker.obtainMessage(Constants.MESSAGE_SDK_UPLOAD_OK,0,0).sendToTarget();
                }
            }
        }

        private void checkAppSee() {
            try {
                String url;
                if (appInfo.ZGSeePolicyUrl == null){
                    url = Constants.ZG_BASE_API + "/appkey/" + appInfo.getAppKey();
                }else {
                    url = appInfo.ZGSeePolicyUrl;
                }

                byte[] data = httpService.sendRequest(url, null, null);
                JSONObject obj = null;
                if (data != null){
                    String s = new String(data, "UTF-8");
                    obj = new JSONObject(s);
                }
                Message result = zgWorker.obtainMessage(Constants.MESSAGE_CHECK_APP_SEE_RETURN);
                result.obj = obj;
                result.sendToTarget();
            }catch (Exception e){
                ZGLogger.handleException(HTTP_TAG,"check app see error.",e);
            }
        }

        private void uploadZGSee(JSONObject data, int arg1) {
            boolean success = false;
            try {
                byte[] compress = Utils.compress(data.toString().getBytes("UTF-8"));

                String base64String = Base64.encodeToString(compress,Base64.NO_WRAP);
                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("event",base64String);
                byte[] bytes = builder.build().getEncodedQuery().getBytes("UTF-8");
                String url;
                if (appInfo.ZGSeeUrl == null){
                    url = Constants.ZG_BASE_API + "/sdk_zgsee";
                }else {
                    url = appInfo.ZGSeeUrl;
                }
                byte[] res = httpService.sendRequest(url,null,bytes);
                String s = new String(res,"UTF-8");
                ZGLogger.logMessage(HTTP_TAG,"upload return:" + s);
                success = true;
            }catch (Exception e){
                ZGLogger.handleException(HTTP_TAG,"upload ZGSee error.",e);
            }finally {
                if (success){
                    zgWorker.obtainMessage(Constants.MESSAGE_ZGSEE_UPLOAD_OK,arg1,0).sendToTarget();
                }else {
                    zgWorker.obtainMessage(Constants.MESSAGE_ZGSEE_UPLOAD_OK,0,0).sendToTarget();
                }
            }
        }
    }

}
