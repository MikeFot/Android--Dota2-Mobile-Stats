package com.michaelfotiadis.mobiledota2.network;

import com.google.gson.Gson;
import com.michaelfotiadis.mobiledota2.network.api.HeroStatsApi;
import com.michaelfotiadis.steam.net.OkHttpFactory;

import retrofit2.Retrofit;

public class RestClient {

    private final Retrofit2Factory mRetrofit2Factory;

    public RestClient(final Gson gson,
                      final OkHttpFactory factory) {

        mRetrofit2Factory = new Retrofit2Factory(gson, factory);

    }

    public HeroStatsApi getHeroStatsApi() {
        return createApi(HeroStatsApi.class);
    }


    private synchronized <T> T createApi(final Class<T> clazz) {
        final Retrofit retrofit = mRetrofit2Factory.create(clazz);
        return retrofit.create(clazz);
    }


}
