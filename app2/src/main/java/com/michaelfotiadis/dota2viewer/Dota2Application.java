package com.michaelfotiadis.dota2viewer;

import android.app.Application;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.db.DatabaseCreator;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.injection.ComponentStore;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

public class Dota2Application extends Application implements LifecycleRegistryOwner {

    @Inject
    ActivityLifeCycleMonitor mActivityLifeCycleMonitor;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    UserPreferences mUserPreferences;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        mLifecycleRegistry = new LifecycleRegistry(this);

        LeakCanary.install(this);
        initCrashlytics();
        JodaTimeAndroid.init(this);

        Injector.setComponentStore(new ComponentStore(this, getLifecycle(), BuildConfig.DEBUG, BuildConfig.DEV_MODE));
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);

        //register activity lifecycle handler
        mActivityLifeCycleMonitor.setOnVisibilityChangeListener(new VisibilityChangeListener());
        registerActivityLifecycleCallbacks(mActivityLifeCycleMonitor);

        DatabaseCreator.getInstance().createDb(this);

        //noinspection AnonymousInnerClassMayBeStatic
        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String id) {
                AppLog.d("Posting user changed event");
                EventBus.getDefault().post(new UserChangedEvent(id));
            }
        });

    }

    private void initCrashlytics() {

        final Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit, new Crashlytics());

    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    private class VisibilityChangeListener implements ActivityLifeCycleMonitor.OnVisibilityChangeListener {
        @Override
        public void onApplicationHidden() {
            AppLog.d("App closing");
            mJobScheduler.clear();
        }

        @Override
        public void onApplicationVisible() {
            // NOOP
        }

        @Override
        public void onApplicationFocused() {
            // NOOP
        }
    }

}
