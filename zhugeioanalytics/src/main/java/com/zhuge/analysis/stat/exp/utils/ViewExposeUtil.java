package com.zhuge.analysis.stat.exp.utils;

/**
 * Created by Sure on 2021/6/26.
 */

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewParent;

import com.zhuge.analysis.util.ZGLogger;

public final class ViewExposeUtil {

    public static Activity findActivity(Context context) {
        if (!(context instanceof ContextWrapper)) {
            return null;
        } else {
            ContextWrapper current;
            Context parent;
            for (current = (ContextWrapper) context; !(current instanceof Activity); current =
                    (ContextWrapper) parent) {
                parent = current.getBaseContext();
                if (!(parent instanceof ContextWrapper)) {
                    return null;
                }
            }

            return (Activity) current;
        }
    }

    public static boolean isDestroy(Context context) {
        Activity activity = findActivity(context);
        return activity != null && VERSION.SDK_INT >= 17 && activity.isDestroyed();
    }

    public static boolean viewVisibleInParents(View view) {
        if (view == null) {
            return false;
        } else if (isViewSelfInvisible(view)) {
            return false;
        } else {
            ViewParent viewParent = view.getParent();

            do {
                if (!(viewParent instanceof View)) {
                    return true;
                }

                if (isViewSelfInvisible((View) viewParent)) {
                    return false;
                }

                viewParent = viewParent.getParent();
            } while (viewParent != null);

            ZGLogger.logMessage("ZhugeExpose", "get view parent is not a view: ");
            return false;
        }
    }

    public static boolean isViewSelfInvisible(View mView) {
        if (mView != null && mView.getWindowVisibility() != View.GONE) {
            if (mView.getWidth() > 0 && mView.getHeight() > 0 && mView.getAlpha() > 0.0F && mView.getLocalVisibleRect(new Rect())) {
                if (mView.getVisibility() != View.VISIBLE && mView.getAnimation() != null && mView.getAnimation().getFillAfter()) {
                    return false;
                } else {
                    return mView.getVisibility() != View.VISIBLE;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
