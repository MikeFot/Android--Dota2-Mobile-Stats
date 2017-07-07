package com.michaelfotiadis.mobiledota2.network.api;

import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HeroStatsApi {


    @GET("heroes/all")
    Call<Map<String, HeroDetails>> getDetailsAllHeroes();

    @GET("heroes/{hero_name}")
    Call<Map<String, HeroDetails>> getHeroDetails(@Path("hero_name") String heroNameOrId);

    @GET("heroes")
    Call<Map<String, String>> getHeroKeys();


}
