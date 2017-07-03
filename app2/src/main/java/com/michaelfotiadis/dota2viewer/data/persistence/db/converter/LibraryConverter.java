package com.michaelfotiadis.dota2viewer.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.steam.player.library.Library;

public class LibraryConverter {

    @TypeConverter
    public static Library toModel(final String value) {
        return value == null ? null : new Gson().fromJson(value, Library.class);
    }

    @TypeConverter
    public static String toJson(final Library library) {
        return library == null ? null : new Gson().toJson(library);
    }
}