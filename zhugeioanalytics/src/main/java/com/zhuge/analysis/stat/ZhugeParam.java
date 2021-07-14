package com.zhuge.analysis.stat;

import com.zhuge.analysis.listeners.ZhugeInAppDataListener;

/**
 * Created by Omen on 2018/6/19.
 */
public class ZhugeParam {
    final String appKey;
    final String appChannel;
    final String did;
    final ZhugeInAppDataListener listener;

    private ZhugeParam(Builder builder) {
        this.appKey = builder.appKey;
        this.appChannel = builder.appChannel;
        this.did = builder.did;
        this.listener = builder.zhugeInAppDataListener;
    }

    @Override
    public String toString() {

        return "appKey: "+appKey+" , appChannel:"+ appChannel+" , did: "+did;
    }

    public static final class Builder {
        private String appKey = null;
        private String appChannel = null;
        private String did = null;
        private ZhugeInAppDataListener zhugeInAppDataListener = null;
        public Builder(){
        }

        public Builder appKey(String appkey){
            this.appKey = appkey;
            return this;
        }

        public Builder appChannel(String appChannel){
            this.appChannel = appChannel;
            return this;
        }

        public Builder did(String did){
            this.did = did;
            return this;
        }

        public Builder inAppDataListener(ZhugeInAppDataListener listener){
            this.zhugeInAppDataListener = listener;
            return this;
        }


        public ZhugeParam build(){

            return new ZhugeParam(this);
        }
    }
}