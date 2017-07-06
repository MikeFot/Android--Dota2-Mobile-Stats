package com.michaelfotiadis.dota2viewer.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

public class DotaRarityConverter {
    @TypeConverter
    public static Rarity toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, Rarity.class);
    }

    @TypeConverter
    public static String toJson(final Rarity rarity) {
        return rarity == null ? null : new Gson().toJson(rarity);
    }
}