package com.zhugeioanalytics.android.plugin

class ZhugeioAnalyticsSDKExtension {
    // 同ZhugeioAnalyticsSDKHookConfig中方法对应, disableIMEI,disableLog,disableJsInterface，disableAndroidID
    boolean disableIMEI = false
    boolean disableLog = false
    boolean disableJsInterface = false
    boolean disableAndroidID = false
    boolean disableMacAddress = false
    boolean disableCarrier = false

    @Override
    String toString() {
        return "\t\tdisableIMEI=" + disableIMEI + "\n" +
                "\t\tdisableLog=" + disableLog + "\n" +
                "\t\tdisableJsInterface=" + disableJsInterface + "\n" +
                "\t\tdisableAndroidID=" + disableAndroidID + "\n" +
                "\t\tdisableMacAddress=" + disableMacAddress + "\n" +
                "\t\tdisableCarrier=" + disableCarrier
    }
}

