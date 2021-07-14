package com.zhuge.analysis.stat;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.zhuge.analysis.util.ZGLogger;

/**
 * SDK常量信息类
 * Created by Omen on 16/6/17.
 */
public class Constants {


    /*package*/ static final String KEY_SOURCE = "utm_source";
    /*package*/ static final String KEY_MEDIUM = "utm_medium";
    /*package*/ static final String KEY_CAMPAIGN = "utm_campaign";
    /*package*/ static final String KEY_CONTENT = "utm_content";
    /*package*/ static final String KEY_TERM = "utm_term";
    /*package*/ static final String KEY_TYPE = "utm_type";
    /*package*/ static final String KEY_DID = "$zg_did";


    /**
     * 收入事件 Name
     */
    public static final String ZhugeEventRevenue = "revenue";
    /**
     * 收入事件追踪 Property
     */
    public static final String ZhugeEventRevenuePrice = "price";
    public static final String ZhugeEventRevenueProductID = "productID";
    public static final String ZhugeEventRevenueProductQuantity = "productQuantity";
    public static final String ZhugeEventRevenueType = "revenueType";
    public static final String ZhugeEventRevenueTotalPrice = "total";


    /*package*/ static final int CODE_NEED_FLUSH = 0;

    /*package*/ static final int MESSAGE_DEVICE_INFO = 1;
    /*package*/ static final int MESSAGE_CHECK_SESSION = 2;
    /*package*/ static final int MESSAGE_CUSTOM_EVENT = 3;
    /*package*/ static final int MESSAGE_IDENTIFY_USER = 4;
    /*package*/ static final int MESSAGE_FLUSH = 5;
    /*package*/ static final int MESSAGE_NEED_SEND = 6;
    /*package*/ static final int MESSAGE_UPDATE_SESSION = 7;
    /*package*/ static final int MESSAGE_START_TRACK = 8;
    /*package*/ static final int MESSAGE_END_TRACK = 9;
    /*package*/ static final int MESSAGE_SET_EVENT_INFO = 10;
    /*package*/ static final int MESSAGE_SET_DEVICE_INFO = 11;
    /*package*/ static final int MESSAGE_SEND_SCREENSHOT = 12;
    /*package*/ static final int MESSAGE_CHECK_APP_SEE = 13;
    /*package*/ static final int MESSAGE_ZGSEE_UPLOAD_OK = 14;
    /*package*/ static final int MESSAGE_ZGSEE_CHECK_LOCAL = 15;
    /*package*/ static final int MESSAGE_SDK_UPLOAD_OK = 16;
    /*package*/ static final int MESSAGE_AUTO_TRACK = 17;
    /*package*/ static final int MESSAGE_CHECK_APP_SEE_RETURN = 18;
    /*package*/ static final int MESSAGE_REVENUE_EVENT = 19;
    /*package*/ static final int MESSAGE_DURATION_EVENT = 20;

    public static final String SDK_V = "3.4.16";
//    static int UPLOAD_LIMIT_SIZE = 5;
    static int UPLOAD_LIMIT_SIZE = 1;
    static long FLUSH_INTERVAL = 5*1000;
    static int MAX_LOCAL_SIZE = 3000;
    public static final int MAX_SEE_SESSION = 5;
    /**
     * 每天发送事件数
     */
    static  int SEND_SIZE = 50000;
    static final boolean ENABLE_SESSION_TRACK = true;//当前默认开启，之后可能关闭st,se事件追踪

    public static String ZG_BASE_API = "https://u.zhugeapi.com";
    public static String ZG_BACKUP_API = "https://ubak.zhugeio.com";


    public static String API_PATH = "https://u.zhugeapi.com/apipool";
    public static String API_PATH_BACKUP = "https://ubak.zhugeio.com/upload/";

    public static final String PATH_ENDPOINT = "apipool";
    public static final String ZGSEE_ENDPOINT = "sdk_zgsee";

    public static final String BACKUP_PATH_ENDPOINT = "upload";
    public static final String ZGSEE_CHECK_ENDPOINT = "appkey/default";

//    public static final String API_CHECK_APP_SEE = "https://ubak.zhugeio.com/appkey/default";
//    public static String API_ZGSEE_PATH = "https://ubak.zhugeio.com/sdk_zgsee";
    /**
     * 会话上次活动时间
     */
    static  String SP_LAST_SESSION_TIME = "ZhugeLastSession";
    /**
     * 今日事件数
     */
    static  String SP_TODAY_COUNT = "Today_total";

    /**
     * 本地之前是否有开启过zhuge see
     */
    static  String SP_ENABLE_ZGSEE = "zhuge_see";

    /**
     * 设备信息更新时间
     */
    static  String SP_UPDATE_DEVICE_TIME = "info_ts";

    /**
     * 设备ID
     */
    static  String SP_DID = "zhuge_did";

    /**
     * 用户ID
     */
    static  String SP_CUID = "cuid";
    /**
     * 会话事件计数
     */
    static String SP_SESSION_COUNT = "sc";

    /**
     *
     */
    static String SP_SEE_COUNT = "see_sc";

    /**
     * 最后进入的页面
     */
    static  String SP_LAST_PAGE = "last_page";

    /**
     * 会话间隔,单位毫秒。
     */
    static  int SESSION_EXCEED = 30 * 1000;

    static String SP_MODIFY = "zg_update_status";

    static String SP_FIRST_SCREENSHOT_TIME = "zg_first_screen_time";

    static final String SP_USER_DEFINE_DEVICE = "zg_user_device";

    static final String SP_USER_DEFINE_EVENT = "zg_user_event";

    static void configString(StringBuilder stringBuilder) {
        stringBuilder.append("SDK版本: ").append(SDK_V).append("\n")
                .append("触发上传事件数: ").append(UPLOAD_LIMIT_SIZE).append("\n")
                .append("本地最大缓存数: ").append(MAX_LOCAL_SIZE).append("\n")
                .append("每日上传事件数: ").append(SEND_SIZE).append("\n")
                .append("会话间隔: ").append(SESSION_EXCEED).append("\n");


    }
    static void loadConfig(Context context){
        try {
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            if (null == metaData ){
                metaData = new Bundle();
            }
            SESSION_EXCEED = (metaData.getInt("com.zhuge.config.SessionInterval",30))*1000; //会话间隔，配置以秒计算
            UPLOAD_LIMIT_SIZE = metaData.getInt("com.zhuge.config.UploadLimit",1); //1条记录上传
            FLUSH_INTERVAL = (metaData.getInt("com.zhuge.config.FlushInterval",5))*1000; //数据上传间隔，默认5秒
            MAX_LOCAL_SIZE = metaData.getInt("com.zhuge.config.MaxLocalSize",3000); //本地最大缓存数，默认3000
            SEND_SIZE = metaData.getInt("com.zhuge.config.MaxSendSize",50000); //每日最大上传数，默认50000
        }catch (Exception e){
            ZGLogger.handleException("Zhuge.Constants","读取配置信息出错，将使用默认配置",e);
        }
    }


}
