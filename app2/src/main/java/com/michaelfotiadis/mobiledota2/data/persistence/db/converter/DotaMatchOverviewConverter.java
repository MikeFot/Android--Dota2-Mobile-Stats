package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

public class DotaMatchOverviewConverter {
    @TypeConverter
    public static MatchOverview toModel(String value) {
        return value == null ? null : new Gson().fromJson(value, MatchOverview.class);
    }

    @TypeConverter
    public static String toJson(MatchOverview item) {
        return item == null ? null : new Gson().toJson(item);
    }
}