package com.zhuge.analysis.stat.exp.utils;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by Sure on 2021/6/24.
 */
public class TimerToggle implements Runnable {
    private final Handler mHandler;
    private final Runnable mAction;
    private long mFirstToggleTime;
    private long mDelayTime;
    private long mMaxDelayTime;
    private boolean mFirstTimeDelay;

    private TimerToggle(Runnable action) {
        this.mFirstToggleTime = -1L;
        this.mHandler = new Handler();
        this.mAction = action;
    }

    public void toggle() {
        long currentTime = SystemClock.uptimeMillis();
        if (this.mDelayTime == 0L) {
            this.takeAction();
        } else if (this.mFirstToggleTime == -1L && !this.mFirstTimeDelay) {
            this.takeAction();
        } else if (this.mFirstToggleTime > 0L && currentTime - this.mFirstToggleTime >= this.mMaxDelayTime) {
            this.takeAction();
        } else {
            if (this.mFirstToggleTime <= 0L) {
                this.mFirstToggleTime = currentTime;
            }

            this.mHandler.removeCallbacks(this);
            long targetTime = Math.min(this.mFirstToggleTime + this.mMaxDelayTime,
                    currentTime + this.mDelayTime);
            this.mHandler.postAtTime(this, targetTime);
        }

    }

    void takeAction() {
        this.mHandler.removeCallbacks(this);
        this.mFirstToggleTime = 0L;
        this.mAction.run();
    }

    public void reset() {
        this.mFirstToggleTime = -1L;
        this.mHandler.removeCallbacks(this);
    }

    public void run() {
        this.takeAction();
    }

    public static class Builder {
        private long delayTime = 50L;
        private long maxDelayTime = 600L;
        private Runnable action;
        private boolean firstTimeDelay = true;

        public Builder(Runnable action) {
            this.action = action;
        }

        public TimerToggle.Builder delayTime(long delayTime) {
            this.delayTime = delayTime;
            return this;
        }

        public TimerToggle.Builder maxDelayTime(long maxDelayTime) {
            this.maxDelayTime = maxDelayTime;
            return this;
        }

        public TimerToggle.Builder firstTimeDelay(boolean firstTimeDelay) {
            this.firstTimeDelay = firstTimeDelay;
            return this;
        }

        public TimerToggle build() {
            TimerToggle toggler = new TimerToggle(this.action);
            toggler.mMaxDelayTime = this.maxDelayTime;
            toggler.mDelayTime = this.delayTime;
            toggler.mFirstTimeDelay = this.firstTimeDelay;
            return toggler;
        }
    }
}
