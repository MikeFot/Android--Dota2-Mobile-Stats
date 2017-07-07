package com.michaelfotiadis.mobiledota2.event.listener;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.michaelfotiadis.mobiledota2.utils.AppLog;

import org.greenrobot.eventbus.EventBus;

public class EventLifecycleListener<T> implements LifecycleObserver {

    private final T mParent;
    private final Lifecycle mLifecycle;

    public EventLifecycleListener(final T parent, final Lifecycle lifecycle) {
        mParent = parent;
        mLifecycle = lifecycle;
    }

    public void enable() {
        AppLog.d("Lifecycle observer added for class " + getTag());
        mLifecycle.addObserver(this);
        registerEventBus();
    }

    public void disable() {
        AppLog.d("Lifecycle observer removed for class " + getTag());
        mLifecycle.removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        registerEventBus();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(mParent)) {
            EventBus.getDefault().register(mParent);
            AppLog.d("Event bus registered for class " + getTag());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        unregisterEventBus();
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(mParent)) {
            EventBus.getDefault().unregister(mParent);
            AppLog.d("Event bus unregistered for class " + getTag());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        disable();
    }

    private String getTag() {
        return mParent.getClass().getSimpleName();
    }

}