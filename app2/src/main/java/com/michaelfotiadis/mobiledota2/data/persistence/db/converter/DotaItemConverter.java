package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

public class DotaItemConverter {
    @TypeConverter
    public static GameItem toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, GameItem.class);
    }

    @TypeConverter
    public static String toJson(final GameItem item) {
        return item == null ? null : new Gson().toJson(item);
    }
}