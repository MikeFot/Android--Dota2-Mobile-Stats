package com.michaelfotiadis.dota2viewer.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;

public class HeroDetailsConverter {

    @TypeConverter
    public static HeroDetails toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, HeroDetails.class);
    }

    @TypeConverter
    public static String toJson(final HeroDetails hero) {
        return hero == null ? null : new Gson().toJson(hero);
    }

}