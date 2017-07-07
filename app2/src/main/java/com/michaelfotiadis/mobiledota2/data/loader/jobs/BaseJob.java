package com.michaelfotiadis.mobiledota2.data.loader.jobs;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.michaelfotiadis.mobiledota2.event.JobFinishedEvent;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseJob extends Job implements Comparable<BaseJob> {

    protected static String DEFAULT_LANGUAGE = "en_us";
    private final long mCreationTime;

    protected BaseJob() {
        super(new Params(1).delayInMs(0));
        mCreationTime = System.currentTimeMillis();
    }

    public long getCreationTime() {
        return mCreationTime;
    }

    protected void postJobFinished() {
        AppLog.d("Posting " + getClass().getSimpleName() + " job finished");
        postEvent(new JobFinishedEvent());
    }

    protected <T> void postEvent(final T event) {
        EventBus.getDefault().post(event);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull final Throwable throwable,
                                                     final int runCount, final int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public int compareTo(@NonNull final BaseJob o) {
        return (int) (getCreationTime() - o.getCreationTime());
    }

}
