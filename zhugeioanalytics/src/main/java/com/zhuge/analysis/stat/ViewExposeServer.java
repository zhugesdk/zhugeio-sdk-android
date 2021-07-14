package com.zhuge.analysis.stat;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.zhuge.analysis.stat.exp.ViewExposeAppState;
import com.zhuge.analysis.stat.exp.entities.ViewExposeData;
import com.zhuge.analysis.stat.exp.utils.TimerToggle;
import com.zhuge.analysis.stat.exp.utils.ViewExposeUtil;
import com.zhuge.analysis.util.Utils;
import com.zhuge.analysis.util.ZGLogger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * view 曝光统计
 * Created by Sure on 2021/6/23.
 */
class ViewExposeServer implements ViewExposeAppState.onNewActivityArrivedListener,
        ViewTreeObserver.OnDrawListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "Zhuge.ViewExp";
    /**
     * app生命周期回调，用于获取当前的可视activity
     */
    private final ViewExposeAppState coreAppState;
    /**
     * 当前view tree变动时的回调
     */
    TimerToggle viewTreeChangeTimerToggle;
    WeakHashMap<Activity, ViewExposeServer.ActivityScope> mActivityScopes;

    public ViewExposeServer(ViewExposeAppState coreAppState) {
        this.coreAppState = coreAppState;
        coreAppState.setActivityArrivedListener(this);
        init();
    }

    private void init() {
        if (this.mActivityScopes == null) {
            this.mActivityScopes = new WeakHashMap<>();
            this.viewTreeChangeTimerToggle = (new TimerToggle.Builder(new Runnable() {
                public void run() {
                    ViewExposeServer.this.onGlobalLayout();
                }
            })).delayTime(500L).maxDelayTime(5000L).firstTimeDelay(true).build();
        }
    }

    public void markViewExp(ViewExposeData expose) {
        View view = expose.getView();
        if (view != null) {
            Activity activity = ViewExposeUtil.findActivity(view.getContext());
            if (activity == null) {
                activity = this.coreAppState.getForegroundActivity();
            }

            if (activity == null) {
                ZGLogger.logError(TAG, "can't find the activity of view: " + view);
            } else {
                ZGLogger.logMessage(TAG, "markViewExp: " + expose.getEventName());
                ViewExposeServer.ActivityScope scope = this.mActivityScopes.get(activity);
                if (scope == null) {
                    scope = new ViewExposeServer.ActivityScope(this);
                    this.mActivityScopes.put(activity, scope);
                }

                ExposeEvent event = new ExposeEvent();
                event.expData = expose;
                event.activity = new WeakReference<>(activity);
                if (scope.containView(view)) {
                    ExposeEvent exposeEvent = scope.getExposeEvent(view);
                    if (event.equals(exposeEvent)) {
                        ZGLogger.logError(TAG,
                                "refresh view expose event, and nothing changed: " + expose.getEventName());
                        exposeEvent.expData = event.expData;
                        return;
                    }
                    this.stopViewExposeInternal(scope, view);
                }

                scope.addViewExposeEvent(view, event);
                this.checkAndSendViewTreeChange(activity);
            }
        }
    }


    private void checkAndSendViewTreeChange(Activity activity) {
        Activity resumeActivity = this.coreAppState.getResumedActivity();
        if (resumeActivity != null && activity == resumeActivity) {
            this.viewTreeChangeTimerToggle.toggle();
        }

    }

    private void stopViewExposeInternal(ActivityScope scope, View view) {
        if (scope == null) return;
        ExposeEvent exposeEvent = scope.getExposeEvent(view);
        if (exposeEvent != null) {
            scope.removeView(view);
        }
    }

    public void stopViewExpose(View view) {
        ViewExposeServer.ActivityScope scope = this.findActivityScopeByView(view);
        if (scope != null) {
            this.stopViewExposeInternal(scope, view);
        }
    }

    private ViewExposeServer.ActivityScope findActivityScopeByView(View view) {
        if (this.mActivityScopes != null && view != null) {
            Activity activity = ViewExposeUtil.findActivity(view.getContext());
            if (activity != null) {
                return this.mActivityScopes.get(activity);
            } else {
                Iterator<ActivityScope> var3 = this.mActivityScopes.values().iterator();

                ViewExposeServer.ActivityScope scope;
                do {
                    if (!var3.hasNext()) {
                        return null;
                    }
                    scope = var3.next();
                } while (!scope.containView(view));

                return scope;
            }
        } else {
            return null;
        }
    }

    @Override
    public void newActivityArrived(Activity activity) {
        if (null == activity) return;
        ActivityScope scope = mActivityScopes.get(activity);
        if (scope == null) {
            scope = new ActivityScope(this);
            mActivityScopes.put(activity, scope);
        } else {
            if (scope.activityIsTrack) {
                return;
            }
        }
        Window window = activity.getWindow();
        if (window == null) return;
        View decorView = window.getDecorView();
        if (decorView == null) return;
        ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        if (!viewTreeObserver.isAlive()) {
            return;
        }
        viewTreeObserver.addOnDrawListener(this);
        viewTreeObserver.addOnGlobalLayoutListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            viewTreeObserver.addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    ViewExposeServer.this.onWindowFocusChanged();
                }
            });
        }
        scope.activityIsTrack = true;
    }


    public void onWindowFocusChanged() {
        this.onGlobalLayout();
    }

    @Override
    public void onGlobalLayout() {
        Activity current = this.coreAppState.getResumedActivity();
        if (current != null) {
            this.layoutActivity(current);
        }
    }

    @Override
    public void onDraw() {
        if (this.viewTreeChangeTimerToggle != null) {
            this.viewTreeChangeTimerToggle.toggle();
        }
    }

    /**
     * 当前activity视图状态发生变化，检测view的可见度是否发生变化
     *
     * @param current
     */
    private void layoutActivity(Activity current) {
        if (this.mActivityScopes != null) {
            ViewExposeServer.ActivityScope scope = this.mActivityScopes.get(current);
            if (scope != null) {
                scope.toggleWithViewEvent.toggle();
            }
        }
    }


    /**
     * 发送曝光事件
     *
     * @param exposeEvent
     */
    private void sentExposeEvent(ExposeEvent exposeEvent) {
        ZhugeSDK.getInstance().track(null, exposeEvent.expData.getEventName(),
                exposeEvent.expData.getProp());
    }

    void checkImp(WeakHashMap<View, ExposeEvent> viewTobeExpose) {
        Activity current = this.coreAppState.getResumedActivity();
        if (current != null && this.mActivityScopes != null && this.mActivityScopes.containsKey(current)) {
            ViewExposeServer.ActivityScope scope = this.mActivityScopes.get(current);
            if (viewTobeExpose != null && !viewTobeExpose.isEmpty()) {
                this.viewTreeChangeTimerToggle.reset();
                ArrayList<View> mTmpViewCache = new ArrayList<>();

                Iterator<View> iterator;
                View view;
                try {
                    iterator = viewTobeExpose.keySet().iterator();
                    // 遍历当前的所有view，进行检测
                    while (iterator.hasNext()) {
                        view = iterator.next();
                        ExposeEvent event = viewTobeExpose.get(view);
                        if (event != null) {
                            boolean lastVisible = event.lastVisible;
                            boolean currentVisible = this.checkViewVisibility(event.expData);
                            if (event.expData.getView() != view) {
                                mTmpViewCache.add(view);
                            } else {
                                if (currentVisible && !lastVisible) {
                                    this.sentExposeEvent(event);
                                }
                                event.lastVisible = currentVisible;
                            }
                        }
                    }

                } catch (Exception e) {
                    ZGLogger.printStackTrace(e);
                }

                iterator = mTmpViewCache.iterator();

                while (iterator.hasNext()) {
                    view = iterator.next();
                    this.stopViewExposeInternal(scope, view);
                }

            }
        }
    }

    boolean checkViewVisibility(ViewExposeData mark) {
        View view = mark.getView();
        return ViewExposeUtil.viewVisibleInParents(view);
    }

    static class ExposeEvent {
        ViewExposeData expData;
        boolean lastVisible;
        WeakReference<Activity> activity;

        ExposeEvent() {
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ExposeEvent)) {
                return false;
            } else {
                ExposeEvent other = (ExposeEvent) obj;
                return Utils.objectEquals(this.expData.getEventName(),
                        other.expData.getEventName());
            }
        }
    }

    static class ActivityScope {
        final ToggleWithViewEvent toggleWithViewEvent;
        boolean activityIsTrack = false;

        public ActivityScope(ViewExposeServer server) {
            toggleWithViewEvent = new ToggleWithViewEvent(server);
        }

        public boolean containView(View view) {
            return this.toggleWithViewEvent.containView(view);
        }

        public ExposeEvent getExposeEvent(View view) {
            return this.toggleWithViewEvent.getExposeEvent(view);
        }

        public void addViewExposeEvent(View view, ExposeEvent exposeEvent) {
            toggleWithViewEvent.addViewExposeEvent(view, exposeEvent);
        }

        public void removeView(View view) {
            toggleWithViewEvent.removeView(view);
        }
    }

    static class ToggleWithViewEvent implements Runnable {
        final TimerToggle timerToggle;
        final WeakHashMap<View, ViewExposeServer.ExposeEvent> viewToEvent;
        final ViewExposeServer exposeServer;

        public ToggleWithViewEvent(ViewExposeServer server) {
            this.timerToggle =
                    (new TimerToggle.Builder(this)).maxDelayTime(2000L).build();
            this.exposeServer = server;
            this.viewToEvent = new WeakHashMap<>();
        }

        public boolean containView(View view) {
            return this.viewToEvent.containsKey(view);
        }

        public ExposeEvent getExposeEvent(View view) {
            return this.viewToEvent.get(view);
        }

        public void addViewExposeEvent(View view, ExposeEvent exposeEvent) {
            viewToEvent.put(view, exposeEvent);
        }

        public void removeView(View view) {
            viewToEvent.remove(view);
        }

        public void toggle() {
            this.timerToggle.toggle();
        }

        public void run() {
            this.exposeServer.checkImp(viewToEvent);
        }
    }
}
