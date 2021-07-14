package com.zhuge.analysis.viewSpider;


import com.zhuge.analysis.metrics.Tweaks;

import org.json.JSONArray;

/* This interface is for internal use in the Zhuge library, and should not be
   implemented in client code. */
public interface UpdatesFromZhuge {
    public void startUpdates();

    public void setEventBindings(JSONArray bindings);

    public void setVariants(JSONArray variants);

    public Tweaks getTweaks();
}
