package com.michaelfotiadis.mobiledota2.data.loader;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.event.JobFinishedEvent;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JobPriorityQueue {

    private static final long DELAY = TimeUnit.SECONDS.toMillis((1));
    private long mLastFetchedTime = Long.MIN_VALUE;
    private static final int MIN_CONSUMERS = 0;
    private static final int MAX_CONSUMERS = 1;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private final JobManager mJobManager;
    private final PriorityQueue<BaseJob> mQueue;

    public JobPriorityQueue(final Context context,
                            final Lifecycle lifecycle) {

        mJobManager = new JobManager(getConfiguration(context));
        mQueue = new PriorityQueue<>();

        final InternalLifecycleObserver internalLifecycleObserver = new InternalLifecycleObserver(this, lifecycle);
        internalLifecycleObserver.enable();

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onJobFinishedEvent(final JobFinishedEvent event) {
        AppLog.d("Job finished for event " + event.hashCode());
        mLastFetchedTime = System.currentTimeMillis();
        queueNextJob();
    }

    void clear() {
        mQueue.clear();
    }

    void queueJob(final BaseJob job) {

        mQueue.add(job);

        if (mQueue.size() == 1) {
            queueNextJob();
        }

    }

    private void queueNextJob() {

        // only queue jobs after a 1 second delay
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    // Moves the current Thread into the background
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    AppLog.d("Queueing next job. Jobs remaining= " + mQueue.size());
                    try {
                        if (System.currentTimeMillis() - mLastFetchedTime < DELAY && mLastFetchedTime != Long.MIN_VALUE) {
                            AppLog.d("Waiting " + DELAY);
                            wait(DELAY);
                        } else {
                            AppLog.d("No need to delay");
                        }
                        final Job nextJob = mQueue.poll();
                        if (nextJob == null) {
                            AppLog.d("No more jobs to queue");
                            return;
                        }
                        AppLog.d("Job started at " + System.currentTimeMillis());
                        mJobManager.addJobInBackground(nextJob);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        EXECUTOR_SERVICE.submit(task);
    }


    private static Configuration getConfiguration(final Context context) {

        return new Configuration.Builder(context)
                .minConsumerCount(MIN_CONSUMERS)
                .maxConsumerCount(MAX_CONSUMERS)
                .consumerKeepAlive(20)
                .resetDelaysOnRestart()
                .customLogger(new JobManagerLogger()).build();

    }

    static class InternalLifecycleObserver implements LifecycleObserver {

        private final Lifecycle mLifecycle;
        private final JobPriorityQueue mJobPriorityQueue;

        InternalLifecycleObserver(final JobPriorityQueue jobPriorityQueue, final Lifecycle lifecycle) {
            mJobPriorityQueue = jobPriorityQueue;
            mLifecycle = lifecycle;
        }

        void enable() {
            mLifecycle.addObserver(this);
            EventBus.getDefault().register(mJobPriorityQueue);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {
            mJobPriorityQueue.clear();
            mLifecycle.removeObserver(this);
            EventBus.getDefault().unregister(mJobPriorityQueue);
        }

    }

}
