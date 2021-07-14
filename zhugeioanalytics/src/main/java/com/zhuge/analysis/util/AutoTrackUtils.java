package com.zhuge.analysis.util;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Omen on 2019-07-19.
 */
public class AutoTrackUtils {

    private static final String TAG = "autoTrack";
    /**
     * 获取 Activity 的 title
     *
     * @param activity Activity
     * @return Activity 的 title
     */
    public static String getActivityTitle(Activity activity) {
        String activityTitle = null;
        try {
            long s = System.currentTimeMillis();
            if (activity != null) {
                if (!TextUtils.isEmpty(activity.getTitle())) {
                    activityTitle = activity.getTitle().toString();
                }
                if (TextUtils.isEmpty(activityTitle)) {
                    String toolbarTitle = getToolbarTitle(activity);
                    if (!TextUtils.isEmpty(toolbarTitle)) {
                        activityTitle = toolbarTitle;
                    }
                }
                if (TextUtils.isEmpty(activityTitle)) {
                    PackageManager packageManager = activity.getPackageManager();
                    if (packageManager != null) {
                        ActivityInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), 0);
                        if (!TextUtils.isEmpty(activityInfo.loadLabel(packageManager))) {
                            activityTitle = activityInfo.loadLabel(packageManager).toString();
                        }
                    }
                }
            }
            long dur = System.currentTimeMillis() - s;
            ZGLogger.logVerbose("get title cast "+ dur);

        } catch (Exception e) {
            return null;
        }
        return activityTitle;
    }

    @TargetApi(11)
    static String getToolbarTitle(Activity activity) {
        try {
            if ("com.tencent.connect.common.AssistActivity".equals(activity.getClass().getCanonicalName())) {
                if (!TextUtils.isEmpty(activity.getTitle())) {
                    return activity.getTitle().toString();
                }
                return null;
            }
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if (!TextUtils.isEmpty(actionBar.getTitle())) {
                    return actionBar.getTitle().toString();
                }
            } else {
                try {
                    Class<?> appCompatActivityClass = compatActivity();
                    if (appCompatActivityClass != null && appCompatActivityClass.isInstance(activity)) {
                        Method method = activity.getClass().getMethod("getSupportActionBar");
                        Object supportActionBar = method.invoke(activity);
                        if (supportActionBar != null) {
                            method = supportActionBar.getClass().getMethod("getTitle");
                            CharSequence charSequence = (CharSequence) method.invoke(supportActionBar);
                            if (charSequence != null) {
                                return charSequence.toString();
                            }
                        }
                    }
                } catch (Exception e) {
                    //ignored
                }
            }
        } catch (Exception e) {
            ZGLogger.handleException(TAG,"getToolbarTitle error.",e);
        }
        return null;
    }
    private static Class<?> compatActivity() {
        Class<?> appCompatActivityClass = null;
        try {
            appCompatActivityClass = Class.forName("android.support.v7.app.AppCompatActivity");
        } catch (Exception e) {
            //ignored
        }
        if (appCompatActivityClass == null) {
            try {
                appCompatActivityClass = Class.forName("androidx.appcompat.app.AppCompatActivity");
            } catch (Exception e) {
                //ignored
            }
        }
        return appCompatActivityClass;
    }
    public static String getViewIdName(View view){
        String idString = null;
        try {
            if (view.getId() != View.NO_ID) {
                idString = view.getContext().getResources().getResourceEntryName(view.getId());
            }
        } catch (Exception e) {
            //ignore
        }
        return idString;
    }

    public static String getViewText(View child) {
        CharSequence viewText = null;
        if (child instanceof ToggleButton) {
            ToggleButton toggleButton = (ToggleButton) child;
            boolean isChecked = toggleButton.isChecked();
            if (isChecked) {
                viewText = toggleButton.getTextOn();
            } else {
                viewText = toggleButton.getTextOff();
            }
            return viewText == null ?"":viewText.toString();
        }else if (child instanceof TextView) {
            TextView textView = (TextView) child;
            viewText = textView.getText();
            return viewText == null ?"":viewText.toString();
        }
        try {
            Class<?> switchCompatClass = null;
            try {
                switchCompatClass = Class.forName("android.support.v7.widget.SwitchCompat");
            } catch (Exception e) {
                //ignored
            }

            if (switchCompatClass == null) {
                try {
                    switchCompatClass = Class.forName("androidx.appcompat.widget.SwitchCompat");
                } catch (Exception e) {
                    //ignored
                }
            }

            if (switchCompatClass != null && switchCompatClass.isInstance(child)) {
                CompoundButton switchCompat = (CompoundButton) child;
                Method method;
                if (switchCompat.isChecked()) {
                    method = child.getClass().getMethod("getTextOn");
                } else {
                    method = child.getClass().getMethod("getTextOff");
                }
                viewText = (String) method.invoke(child);
            } else if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                if (!TextUtils.isEmpty(imageView.getContentDescription())) {
                    viewText = imageView.getContentDescription().toString();
                }
            } else {
                viewText = child.getContentDescription();
            }
            if (!TextUtils.isEmpty(viewText)) {
                return viewText.toString();
            }
        } catch (Exception ex) {
            ZGLogger.handleException(TAG,"get view text error.",ex);
        }
        return "";
    }
    private static int getChildIndex(ViewParent parent, View child) {
        try {
            if (!(parent instanceof ViewGroup)) {
                return -1;
            }

            ViewGroup viewParent = (ViewGroup) parent;
            String childClassName = child.getClass().getCanonicalName();
            int index = 0;
            for (int i = 0; i < viewParent.getChildCount(); i++) {
                View brother = viewParent.getChildAt(i);

                if (!hasClassName(brother, childClassName)) {
                    continue;
                }

                if (brother == child) {
                    return index;
                }

                index++;
            }

            return -1;
        } catch (Exception e) {
            ZGLogger.handleException(TAG,"get child index error;",e);
            return -1;
        }
    }

    private static String getViewId(View view) {
        String idString = null;
        try {
            if (view.getId() != View.NO_ID) {
                idString = view.getContext().getResources().getResourceEntryName(view.getId());
            }
        } catch (Exception e) {
            //ignore
        }
        return idString;
    }

    public static String getViewPath(View view) {
        if (view == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ViewParent viewParent;
            Deque<String> viewPath = new LinkedList<>();
            do {
                viewParent = view.getParent();
                int index = getChildIndex(viewParent, view);
                viewPath.push(view.getClass().getSimpleName() + "[" + index + "]");
                if (viewParent instanceof ViewGroup) {
                    view = (ViewGroup) viewParent;
                }
            } while (viewParent instanceof ViewGroup);

            while (!viewPath.isEmpty()){
                stringBuilder.append(viewPath.poll());
                if (!viewPath.isEmpty()){
                    stringBuilder.append("/");
                }
            }
        } catch (Exception e) {
            ZGLogger.handleException(TAG,"get view path error;",e);
        }
        return stringBuilder.toString();
    }

    public static boolean hasClassName(Object o, String className) {
        Class<?> klass = o.getClass();
        while (klass.getCanonicalName() != null) {
            if (klass.getCanonicalName().equals(className)) {
                return true;
            }

            if (klass == Object.class) {
                break;
            }

            klass = klass.getSuperclass();
        }
        return false;
    }

    public static Activity getActivityFromContext(Context context, View view) {
        Activity activity = null;
        try {
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                } else {
//                    if (view != null) {
//                        Object object = view.getTag(R.id.zhugeio_analytics_tag_view_activity);
//                        if (object != null) {
//                            if (object instanceof Activity) {
//                                activity = (Activity) object;
//                            }
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            ZGLogger.logError(TAG,e.toString());
        }
        return activity;
    }

    public static String traverseView(StringBuilder stringBuilder, ViewGroup root) {
        try {
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder();
            }

            if (root == null) {
                return stringBuilder.toString();
            }

            final int childCount = root.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                final View child = root.getChildAt(i);

                if (child.getVisibility() != View.VISIBLE) {
                    continue;
                }

                if (child instanceof ViewGroup) {
                    traverseView(stringBuilder, (ViewGroup) child);
                } else {
                    if (isViewIgnored(child)) {
                        continue;
                    }

                    String viewText = getViewText(child);
                    if (!TextUtils.isEmpty(viewText)) {
                        stringBuilder.append(viewText);
                        stringBuilder.append("-");
                    }
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return stringBuilder != null ? stringBuilder.toString() : "";
        }
    }


    /**
     * 判断 View 是否被忽略
     * 暂时 只判断 null
     */
    public static boolean isViewIgnored(View view) {
        try {
            //基本校验
            if (view == null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return true;
        }
    }
}