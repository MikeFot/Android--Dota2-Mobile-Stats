package com.michaelfotiadis.mobiledota2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.michaelfotiadis.mobiledota2.utils.AppLog;

public class ActivityLifeCycleMonitor implements Application.ActivityLifecycleCallbacks {

    private static final boolean DEBUG_ENABLED = false;

    private int mPaused;
    private int mResumed;
    private int mStarted;
    private int mStopped;
    private boolean mIsVisible = false;
    private OnVisibilityChangeListener mListener;

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        innerLog("onActivityCreated");
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        innerLog("onActivityStarted");
        mStarted++;
        updateState();
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        innerLog("onActivityResumed");
        if (isFocused() && mListener != null) {
            innerLog("onApplicationFocused");
            mListener.onApplicationFocused();
        }
        mResumed++;
        updateState();
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        innerLog("onActivityPaused");
        mPaused++;
        updateState();
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        innerLog("onActivityStopped");
        mStopped++;
        updateState();
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
        innerLog("onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        innerLog("onActivityDestroyed");
    }

    private boolean isFocused() {
        return mResumed == mStopped;
    }

    private void updateState() {
        final boolean newState = inForeground();
        if (mIsVisible != newState) {
            mIsVisible = newState;
            innerLog("Application Visible: " + mIsVisible);

            if (mListener != null) {
                if (mIsVisible) {
                    mListener.onApplicationVisible();
                } else {
                    mListener.onApplicationHidden();
                }
            }

        }
    }

    public boolean inForeground() {
        return mResumed > mStopped;
    }

    private static void innerLog(final String message) {
        if (DEBUG_ENABLED) {
            AppLog.d("ActivityLifecycle: " + message);
        }
    }

    public void setOnVisibilityChangeListener(final OnVisibilityChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnVisibilityChangeListener {
        void onApplicationHidden();

        void onApplicationVisible();

        void onApplicationFocused();
    }

}
