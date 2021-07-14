package com.zhuge.analysis.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AutoTrackHelper {

    private static final String TAG = "ZhugeioAutoTrackHelper";
    private static HashMap<Integer, Long> eventTimestamp = new HashMap<>();

    private static boolean isDeBounceTrack(Object object) {

        return false;
    }

    private static void traverseView(String fragmentName, ViewGroup root) {

    }

    private static boolean isFragment(Object object) {

        return false;
    }

    public static void onFragmentViewCreated(Object object, View rootView, Bundle bundle) {

    }

    private static void trackFragmentAppViewScreen(Object fragment) {

    }

    public static void trackFragmentResume(Object object) {

    }

    private static boolean fragmentGetUserVisibleHint(Object fragment) {
        return false;
    }

    private static boolean fragmentIsHidden(Object fragment) {

        return false;
    }

    public static void trackFragmentSetUserVisibleHint(Object object, boolean isVisibleToUser) {

    }

    private static boolean fragmentIsResumed(Object fragment) {

        return false;
    }

    public static void trackOnHiddenChanged(Object object, boolean hidden) {

    }

    public static void trackExpandableListViewOnGroupClick(ExpandableListView expandableListView, View view,
                                                           int groupPosition) {

    }

    public static void trackExpandableListViewOnChildClick(ExpandableListView expandableListView, View view,
                                                           int groupPosition, int childPosition) {

    }

    public static void trackTabHost(String tabName) {

    }

    public static void trackTabLayoutSelected(Object object, Object tab) {

    }

    public static void trackMenuItem(MenuItem menuItem) {

    }

    public static void trackMenuItem(final Object object, final MenuItem menuItem) {

    }

    public static void trackRadioGroup(RadioGroup view, int checkedId) {

    }

    public static void trackDrawerOpened(View view) {

    }

    public static void trackDrawerClosed(View view) {

    }

    public static void trackListView(AdapterView<?> adapterView, View view, int position) {

    }

    public static void showChannelDebugActiveDialog(final Activity activity) {

    }

    public static void trackDialog(DialogInterface dialogInterface, int whichButton) {

    }

    public static void trackViewOnClick(View view) {
        if (view == null) {
            return;
        }
        trackViewOnClick(view, view.isPressed());
    }

    public static void trackViewOnClick(View view, boolean isFromUser) {

    }

    public static void loadUrl(View webView, String url) {
        loadUrl2(webView,url);
        invokeWebViewLoad(webView, "loadUrl", new Object[]{url}, new Class[]{String.class});
    }

    public static void loadUrl2(View webView, String url) {
        if (webView == null) {
            ZGLogger.logMessage(TAG, "WebView has not initialized.");
            return;
        }
        setupH5Bridge(webView);
    }

    public static void loadUrl(View webView, String url, Map<String, String> additionalHttpHeaders) {
        loadUrl2(webView, url, additionalHttpHeaders);
        invokeWebViewLoad(webView, "loadUrl", new Object[]{url, additionalHttpHeaders}, new Class[]{String.class, Map.class});
    }

    public static void loadUrl2(View webView, String url, Map<String, String> additionalHttpHeaders) {
        if (webView == null) {
            ZGLogger.logMessage(TAG, "WebView has not initialized.");
            return;
        }
        setupH5Bridge(webView);
    }

    public static void loadData(View webView, String data, String mimeType, String encoding) {
        loadData2(webView, data, mimeType, encoding);
        invokeWebViewLoad(webView, "loadData", new Object[]{data, mimeType, encoding}, new Class[]{String.class, String.class, String.class});
    }

    public static void loadData2(View webView, String data, String mimeType, String encoding) {
        if (webView == null) {
            ZGLogger.logMessage(TAG, "WebView has not initialized.");
            return;
        }
        setupH5Bridge(webView);
    }

    public static void loadDataWithBaseURL(View webView, String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        loadDataWithBaseURL2(webView, baseUrl, data, mimeType, encoding, historyUrl);
        invokeWebViewLoad(webView, "loadDataWithBaseURL", new Object[]{baseUrl, data, mimeType, encoding, historyUrl},
                new Class[]{String.class, String.class, String.class, String.class, String.class});
    }

    public static void loadDataWithBaseURL2(View webView, String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        if (webView == null) {
            ZGLogger.logMessage(TAG, "WebView has not initialized.");
            return;
        }
        setupH5Bridge(webView);
    }

    public static void postUrl(View webView, String url, byte[] postData) {
        postUrl2(webView, url, postData);
        invokeWebViewLoad(webView, "postUrl", new Object[]{url, postData},
                new Class[]{String.class, byte[].class});
    }

    public static void postUrl2(View webView, String url, byte[] postData) {
        if (webView == null) {
            ZGLogger.logMessage(TAG, "WebView has not initialized.");
            return;
        }
        setupH5Bridge(webView);
    }

    private static void setupH5Bridge(View webView) {

        if (ZhugeSDK.getInstance().mEnableJavaScriptBridge) {
            setupWebView(webView);
        }

    }

    private static void invokeWebViewLoad(View webView, String methodName, Object[] params, Class[] paramTypes) {

        if (webView == null) {
            ZGLogger.logError(TAG, "WebView has not initialized.");
            return;
        }
        try {
            Class<?> clazz = webView.getClass();
            Method loadMethod = clazz.getMethod(methodName, paramTypes);
            loadMethod.invoke(webView, params);
        } catch (Exception e) {
            ZGLogger.printStackTrace(e);
        }
    }

    static void addWebViewVisualInterface(View webView) {
        if (webView != null) {

        }
    }

    private static boolean isSupportJellyBean() {
        return false;
    }

    private static void setupWebView(View webView) {
        if (webView != null) {
            addJavascriptInterface(webView,new ZhugeSDK.ZhugeJS(),"zhugeTracker");
        }
    }

    private static void addJavascriptInterface(View webView, Object obj, String interfaceName) {
        try {
            Class<?> clazz = webView.getClass();
            try {
                Method getSettingsMethod = clazz.getMethod("getSettings");
                Object settings = getSettingsMethod.invoke(webView);
                if (settings != null) {
                    Method setJavaScriptEnabledMethod = settings.getClass().getMethod("setJavaScriptEnabled", boolean.class);
                    setJavaScriptEnabledMethod.invoke(settings, true);
                }
            } catch (Exception e) {
                //ignore
            }
            Method addJSMethod = clazz.getMethod("addJavascriptInterface", Object.class, String.class);
            addJSMethod.invoke(webView, obj, interfaceName);
        } catch (Exception e) {
            ZGLogger.printStackTrace(e);
        }
    }


    public static void trackRN(Object target, int reactTag, int s, boolean b) {

        if (!ZhugeSDK.getInstance().isEnableAutoTrack()) {
            return;
        } else {
            try {
                JSONObject properties = new JSONObject();
                properties.put(AutoConstants.ELEMENT_TYPE, "RNView");
                if (target != null) {
                    Class<?> clazz = Class.forName("com.facebook.react.uimanager.NativeViewHierarchyManager");
                    Method resolveViewMethod = clazz.getMethod("resolveView", int.class);
                    if (resolveViewMethod != null) {
                        Object object = resolveViewMethod.invoke(target, reactTag);
                        if (object != null) {
                            View view = (View) object;
                            //获取所在的 Context
                            Context context = view.getContext();

                            //将 Context 转成 Activity
                            Activity activity = AutoTrackUtils.getActivityFromContext(context, view);
                            //$screen_name & $title
                            if (activity != null) {
                                properties.put("$page_title", AutoTrackUtils.getActivityTitle(activity));
                                properties.put("$page_name", activity.getClass().getCanonicalName());
                            }
                            if (view instanceof CompoundButton) {//ReactSwitch
                                return;
                            }
                            if (view instanceof TextView) {
                                TextView textView = (TextView) view;
                                if (!(view instanceof EditText) && !TextUtils.isEmpty(textView.getText())) {
                                    properties.put(AutoConstants.ELEMENT_CONTENT, textView.getText().toString());
                                }
                            } else if (view instanceof ViewGroup) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String viewText = AutoTrackUtils.traverseView(stringBuilder, (ViewGroup) view);
                                if (!TextUtils.isEmpty(viewText)) {
                                    viewText = viewText.substring(0, viewText.length() - 1);
                                }
                                properties.put(AutoConstants.ELEMENT_CONTENT, viewText);
                            }
                        }
                    }
                }
                properties.put("$eid","click");

                ZhugeSDK.getInstance().autoTrackEvent(properties);

            } catch (Exception e) {
                ZGLogger.logError("AutTrackerHelp",e.toString());
            }
        }


    }
}
