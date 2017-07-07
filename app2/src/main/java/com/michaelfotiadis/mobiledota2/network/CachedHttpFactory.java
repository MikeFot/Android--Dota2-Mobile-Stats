package com.michaelfotiadis.mobiledota2.network;


import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.net.OkHttpFactory;
import com.michaelfotiadis.steam.net.client.interceptors.HeadersInterceptor;
import com.michaelfotiadis.steam.net.client.interceptors.RetryPolicyInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CachedHttpFactory implements OkHttpFactory {

    private static final long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 Mb
    private final Cache mCache;
    private final NetworkResolver mNetworkResolver;
    private final boolean mIsEnableDebug;

    public CachedHttpFactory(final File cacheFile,
                             final NetworkResolver networkResolver,
                             final boolean enableDebug) {

        mCache = cacheFile != null ? new Cache(cacheFile, SIZE_OF_CACHE) : null;
        this.mNetworkResolver = networkResolver;
        this.mIsEnableDebug = enableDebug;

    }

    public OkHttpClient create(final Class<?> clazz) {

        final List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new RetryPolicyInterceptor());
        interceptors.add(createLoggingInterceptor());

        if (mCache != null && mNetworkResolver != null) {
            AppLog.d("Added caching control interceptor");
            interceptors.add(new CachingControlInterceptor(mNetworkResolver));
        }

        return createClient(interceptors);
    }

    private OkHttpClient createClient(final List<Interceptor> interceptors) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (mCache != null) {
            builder.cache(mCache);
        }

        for (final Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        builder.readTimeout(15, TimeUnit.SECONDS);
        return builder.build();
    }

    private Interceptor createLoggingInterceptor() {

        final HttpLoggingInterceptor.Level level = mIsEnableDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);

        return interceptor;
    }
}
