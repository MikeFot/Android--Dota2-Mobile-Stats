package com.michaelfotiadis.dota2viewer.network;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.net.OkHttpFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*package*/ class Retrofit2Factory {

    private final Gson gson;
    private final OkHttpFactory httpFactory;

    public Retrofit2Factory(final Gson gson,
                            final OkHttpFactory httpFactory) {
        this.httpFactory = httpFactory;
        this.gson = gson;
    }

    public Retrofit create(final Class<?> clazz) {

        final retrofit2.Converter.Factory factory = GsonConverterFactory.create(gson);
        final OkHttpClient client = httpFactory.create(clazz);

        return new Retrofit.Builder()
                .baseUrl("http://api.herostats.io/")
                .client(client)
                .addConverterFactory(factory)
                .build();
    }
}
