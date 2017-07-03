package com.michaelfotiadis.dota2viewer.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

public class DotaMatchDetailsConverter {
    @TypeConverter
    public static MatchDetails toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, MatchDetails.class);
    }

    @TypeConverter
    public static String toJson(final MatchDetails item) {
        return item == null ? null : new Gson().toJson(item);
    }
}