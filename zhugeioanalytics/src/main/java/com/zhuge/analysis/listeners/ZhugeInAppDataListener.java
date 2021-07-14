package com.zhuge.analysis.listeners;
import org.json.JSONObject;

/**
 * Created by zhaohai on 15/11/6.
 */
public interface ZhugeInAppDataListener extends DSFailListener {
    public void zgOnInAppDataReturned(JSONObject initParams);
}
