package com.zhuge.analysis.stat.exp.entities;

import android.view.View;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * view曝光统计数据类
 * Created by Sure on 2021/6/22.
 */
public class ViewExposeData {
    private final WeakReference<View> view;
    private final String eventName;
    private JSONObject prop;

    /**
     * 生成一个view曝光统计参数
     *
     * @param view      要进行曝光监测的view
     * @param eventName 事件名称
     */
    public ViewExposeData(View view, String eventName) {
        this.view = new WeakReference<>(view);
        this.eventName = eventName;
    }

    public View getView() {
        return this.view.get();
    }


    public String getEventName() {
        return this.eventName;
    }


    public JSONObject getProp() {
        return this.prop;
    }

    /**
     * 设置view曝光时的自定义参数
     *
     * @param prop 自定义参数
     */
    public void setProp(JSONObject prop) {
        this.prop = prop;
    }
}
