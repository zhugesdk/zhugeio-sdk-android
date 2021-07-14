package com.zhuge.analysis.util;


import com.zhuge.analysis.deepshare.utils.Log;

import org.json.JSONObject;

/**
 * 诸葛事件信息
 * Created by kongmiao on 14-10-14.
 */
public class ZGJSONObject extends JSONObject {

    public ZGJSONObject() {
        super();
    }


    @Override
    public int hashCode() {
//        int temp = ((Integer) this.optInt("ts") != null ? ((Integer) this.optInt("ts")).hashCode() : 1) ^
//                ((Integer) this.optInt("sid") != null ? ((Integer) this.optInt("sid")).hashCode() : 1) ^
//                (this.optString("et") != null ? this.optString("et").hashCode() : 1);
//
//        Log.i("===----",String.valueOf(temp));

        return ((Integer) this.optInt("ts") != null ? ((Integer) this.optInt("ts")).hashCode() : 1) ^
                ((Integer) this.optInt("sid") != null ? ((Integer) this.optInt("sid")).hashCode() : 1) ^
                (this.optString("et") != null ? this.optString("et").hashCode() : 1);
    }

    @Override
    public boolean equals(Object o) {
        return getCode(this).equals(getCode((ZGJSONObject) o));
    }

    public String getCode(ZGJSONObject o) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.optInt("ts"));
        sb.append(o.optString("et"));
        sb.append(o.optInt("sid"));
        return sb.toString();
    }
}
