package com.michaelfotiadis.mobiledota2.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CachingControlInterceptor implements Interceptor {

    private final NetworkResolver mNetworkResolver;

    public CachingControlInterceptor(final NetworkResolver networkResolver) {
        mNetworkResolver = networkResolver;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request request = chain.request();
        // Add Cache Control only for GET methods
        if ("GET".equals(request.method())) {
            if (mNetworkResolver.isConnected()) {
                request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
            } else {
                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
            }
        }
        return chain.proceed(request);
    }
}