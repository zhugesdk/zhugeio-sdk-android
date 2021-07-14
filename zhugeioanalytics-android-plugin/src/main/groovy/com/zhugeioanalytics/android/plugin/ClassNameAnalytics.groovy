package com.zhugeioanalytics.android.plugin

class ClassNameAnalytics {

    public String className
    boolean isShouldModify = false
    boolean isZhugeioAPI = false
    boolean isZhugeioUtils = false
    boolean isSALog = false
    def methodCells = new ArrayList<ZhugeioAnalyticsMethodCell>()
    boolean isAppWebViewInterface = false

    ClassNameAnalytics(String className) {
        this.className = className
        isZhugeioAPI = (className == 'com.zhuge.analysis.stat.ZhugeSDK')
        isZhugeioUtils = (className == 'com.zhuge.analysis.util.ZhugeioUtils')
        isSALog = (className == 'com.zhuge.analysis.util.ZGLogger')
        isAppWebViewInterface = ((className == 'com.zhuge.analysis.stat.AppWebViewInterface')
                || (className == 'com.zhuge.analysis.stat.visual.WebViewVisualInterface'))
    }

    boolean isSDKFile() {
        return isSALog || isZhugeioAPI || isZhugeioUtils || isAppWebViewInterface
    }

    boolean isLeanback() {
        return className.startsWith("android.support.v17.leanback") || className.startsWith("androidx.leanback")
    }

    boolean isAndroidGenerated() {
        return className.contains('R$') ||
                className.contains('R2$') ||
                className.contains('R.class') ||
                className.contains('R2.class') ||
                className.contains('BuildConfig.class')
    }

}