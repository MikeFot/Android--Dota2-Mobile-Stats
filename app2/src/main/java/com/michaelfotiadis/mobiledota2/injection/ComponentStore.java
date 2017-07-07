package com.michaelfotiadis.mobiledota2.injection;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;

public class ComponentStore {


    private final AndroidAwareComponent mAndroidAwareComponent;

    public ComponentStore(final Application context,
                          final Lifecycle lifecycle,
                          final boolean isDebugEnabled,
                          final boolean isUseTestServer) {

        mAndroidAwareComponent = DaggerAndroidAwareComponent.builder()
                .applicationModule(new ApplicationModule(context, lifecycle, isDebugEnabled, isUseTestServer))
                .build();


    }

    public AndroidAwareComponent getAndroidAwareComponent() {
        return mAndroidAwareComponent;
    }
}
