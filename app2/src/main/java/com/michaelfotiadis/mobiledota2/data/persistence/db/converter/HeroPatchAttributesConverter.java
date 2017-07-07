package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;

public class HeroPatchAttributesConverter {
    @TypeConverter
    public static HeroPatchAttributes toModel(String value) {
        return value == null ? null : new Gson().fromJson(value, HeroPatchAttributes.class);
    }

    @TypeConverter
    public static String toJson(HeroPatchAttributes hero) {
        return hero == null ? null : new Gson().toJson(hero);
    }
}