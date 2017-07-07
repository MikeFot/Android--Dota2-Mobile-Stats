package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

public class DotaHeroConverter {
    @TypeConverter
    public static Hero toModel(String value) {
        return value == null ? null : new Gson().fromJson(value, Hero.class);
    }

    @TypeConverter
    public static String toJson(Hero hero) {
        return hero == null ? null : new Gson().toJson(hero);
    }
}