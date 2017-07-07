package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

public class DotaLeagueConverter {
    @TypeConverter
    public static League toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, League.class);
    }

    @TypeConverter
    public static String toJson(final League item) {
        return item == null ? null : new Gson().toJson(item);
    }
}