# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class  com.zhuge.analysis.stat.ZhugeSDK$ZhugeJS{
   *;
}
-keepattributes Signature,InnerClasses,Deprecated,*Annotation*
-keepattributes Signature,InnerClasses,Deprecated,*Annotation*
-keepattributes EnclosingMethod,JavascriptInterface
-keepattributes Exceptions,SourceFile,LineNumberTable
-keep class com.zhuge.analysis.stat.ZhugeSDK{*;}
-keep enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembernames,allowoptimization enum * {*;}
-keep class com.zhuge.analysis.stat.ZhugeParam{*;}
-keep class com.zhuge.analysis.stat.exp.entities.ViewExposeData{*;}
-keep class com.zhuge.analysis.deepshare.DeepShare { *; }
-keep class com.zhuge.analysis.listeners.ZhugeInAppDataListener { *; }
-keep class  com.zhuge.analysis.stat.ZhugeParam$Builder{
   *;
}



# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

