package com.michaelfotiadis.dota2viewer.data.loader;

import android.util.Log;

import com.birbit.android.jobqueue.log.CustomLogger;

import java.util.Locale;

/*package*/ class JobManagerLogger implements CustomLogger {

    private static final String JOB_MANAGER_LOG_TAG = "JOB_QUEUE_LOG";

    private final String tag;

    JobManagerLogger() {
        this.tag = JOB_MANAGER_LOG_TAG;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void d(final String text, final Object... args) {
        Log.d(tag, String.format(Locale.US, text, args));
    }

    @Override
    public void e(final Throwable t, final String text, final Object... args) {
        Log.e(tag, t.getMessage(), t);
    }

    @Override
    public void e(final String text, final Object... args) {
        Log.e(tag, text);
    }

    @Override
    public void v(final String text, final Object... args) {
        Log.v(tag, String.format(Locale.US, text, args));
    }
}