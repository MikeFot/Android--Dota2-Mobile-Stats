package com.michaelfotiadis.dota2viewer.injection;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.michaelfotiadis.dota2viewer.ActivityLifeCycleMonitor;
import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobPriorityQueue;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.JobFactory;
import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.data.persistence.db.accessor.DbAccessor;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.network.CachedHttpFactory;
import com.michaelfotiadis.dota2viewer.network.ConnectivityUtils;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.network.RestClient;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.SteamLoader;
import com.michaelfotiadis.steam.net.OkHttpFactory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Modifier;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("MethodMayBeStatic")
@Module
public class ApplicationModule {

    private final Context mContext;
    private final Lifecycle mLifecycle;
    private final boolean mIsDebugEnabled;
    private final boolean mIsUseTestServer;

    public ApplicationModule(final Application application,
                             final Lifecycle lifecycle,
                             final boolean isDebugEnabled,
                             final boolean isUseTestServer) {
        mContext = application;
        mLifecycle = lifecycle;
        mIsDebugEnabled = isDebugEnabled;
        mIsUseTestServer = isUseTestServer;

    }

    @Provides
    @Singleton
    DbAccessor providesDbAccessor(final AppDatabase appDatabase) {
        return new DbAccessor(appDatabase);
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase() {
        return Room.databaseBuilder(mContext, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @Provides
    @Named("dev_key")
    String providesDevKey() {

        return new String(Base64.decode(mContext.getString(R.string.key), Base64.DEFAULT));

    }

    @Provides
    File providesCacheFile() {
        return new File(mContext.getCacheDir(), "http");
    }

    @Provides
    NetworkResolver providesNetworkResolver() {
        return new NetworkResolver() {
            @Override
            public boolean isConnected() {
                return ConnectivityUtils.isConnected(mContext);
            }
        };
    }

    @Provides
    OkHttpFactory providesOkHttpFactory(final File cacheFile, final NetworkResolver networkResolver) {

        return new CachedHttpFactory(cacheFile, networkResolver, mIsDebugEnabled);

    }

    @Provides
    RestClient providesRestClient(final Gson gson, final OkHttpFactory okHttpFactory) {
        return new RestClient(gson, okHttpFactory);
    }

    @Provides
    Gson providesGson() {
        return new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .create();
    }

    @Provides
    @Singleton
    SteamLoader provideSteamLoader(@Named("dev_key") final String devKey,
                                   final OkHttpFactory okHttpFactory,
                                   final Gson gson) {

        return new SteamLoader(devKey, mIsUseTestServer, okHttpFactory, gson);

    }

    @Provides
    Lifecycle providesLifecycle() {
        return mLifecycle;
    }

    @Provides
    @Singleton
    JobPriorityQueue providesJobQueue() {
        return new JobPriorityQueue(mContext, mLifecycle);
    }

    @Provides
    JobFactory providesJobFactory() {
        return new JobFactory();
    }

    @Provides
    JobScheduler providesJobScheduler(final JobPriorityQueue jobPriorityQueue,
                                      final JobFactory jobFactory) {
        return new JobScheduler(jobPriorityQueue, jobFactory);
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(final Picasso picasso, final SteamLoader steamLoader) {
        return new ImageLoader(picasso, steamLoader.getImageProvider());
    }

    @Provides
    UserPreferences providesUserPreferences() {
        return new UserPreferences(mContext);
    }

    @Provides
    DataPreferences providesDataPreferences() {
        return new DataPreferences(mContext);
    }

    @Provides
    @Singleton
    Picasso providesPicasso() {
        final Picasso picasso = Picasso.with(mContext.getApplicationContext());
        picasso.setIndicatorsEnabled(mIsDebugEnabled);
        picasso.setLoggingEnabled(mIsDebugEnabled);
        return picasso;
    }

    @Provides
    @Singleton
    ActivityLifeCycleMonitor providesActivityLifecycleMonitor() {
        return new ActivityLifeCycleMonitor();
    }
}
