package com.zhuge.analysis.deepshare.utils;

import com.zhuge.analysis.listeners.utils.Base64;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Util {
    private static final String TAG = "Util";

    public static long startTicks = 0;

    public static JSONArray inappDataTime;
    public static JSONArray generateURLTime;
    public static JSONArray attributeTime;

    public static void initUtil() {
        inappDataTime = new JSONArray();
        generateURLTime = new JSONArray();
        attributeTime = new JSONArray();
    }

    public static JSONObject escapeJSONStrings(JSONObject inputObj) {
        JSONObject filteredObj = new JSONObject();
        if (inputObj != null) {
            Iterator<?> keys = inputObj.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    if (inputObj.has(key) && inputObj.get(key).getClass().equals(String.class)) {
                        filteredObj.put(key, inputObj.getString(key).replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\""));
                    } else if (inputObj.has(key)) {
                        filteredObj.put(key, inputObj.get(key));
                    }
                } catch (JSONException ignore) {
                }
            }
        }
        return filteredObj;
    }

    public static JSONObject getJSONObject(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return new JSONObject();
        } else {
            try {
                return new JSONObject(paramString);
            } catch (JSONException e) {
                byte[] encodedArray = Base64.decode(paramString.getBytes(), Base64.NO_WRAP);
                try {
                    return new JSONObject(new String(encodedArray));
                } catch (JSONException ex) {
                    Log.e(TAG, Log.getStackTraceString(ex));
                    return new JSONObject();
                }
            }
        }
    }

    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }


}


