package com.michaelfotiadis.mobiledota2.data.persistence.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

public class PlayerConverter {
    @TypeConverter
    public static PlayerSummary toModel(final String player) {
        return player == null ? null : new Gson().fromJson(player, PlayerSummary.class);
    }

    @TypeConverter
    public static String toJson(final PlayerSummary playerSummary) {
        return playerSummary == null ? null : new Gson().toJson(playerSummary);
    }
}