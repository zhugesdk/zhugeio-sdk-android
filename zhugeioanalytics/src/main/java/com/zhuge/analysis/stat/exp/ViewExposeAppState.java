package com.zhuge.analysis.stat.exp;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class ViewExposeAppState implements Application.ActivityLifecycleCallbacks {

    public interface onNewActivityArrivedListener {
        void newActivityArrived(Activity activity);
    }

    public static final String TAG = "Zhuge.AppState";
    private WeakReference<Activity> mResumedActivity;
    private WeakReference<Activity> mForeGroundActivity;
    private onNewActivityArrivedListener mListener;

    public void setActivityArrivedListener(onNewActivityArrivedListener listener) {
        mListener = listener;
    }


    public Activity getForegroundActivity() {
        return null == this.mForeGroundActivity ? null : mForeGroundActivity.get();
    }

    public void setForegroundActivity(Activity activity) {
        if (null == activity) {
            this.mForeGroundActivity = null;
            return;
        }
        this.mForeGroundActivity = new WeakReference<>(activity);
    }


    public Activity getResumedActivity() {
        return null == this.mResumedActivity ? null : mResumedActivity.get();
    }

    public void setResumedActivity(Activity activity) {
        if (null == activity) {
            this.mResumedActivity = null;
            return;
        }
        this.mResumedActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.setForegroundActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.setForegroundActivity(activity);
        this.setResumedActivity(activity);
        if (null != mListener) {
            mListener.newActivityArrived(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        this.setResumedActivity(null);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
