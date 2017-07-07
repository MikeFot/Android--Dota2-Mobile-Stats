package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.PlayerConverter;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class PlayerEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(PlayerConverter.class)
    private PlayerSummary playerSummary;

    public PlayerEntity(@NonNull final PlayerSummary playerSummary) {

        this.id = playerSummary.getSteamId();
        this.playerSummary = playerSummary;

    }

    public String getId() {
        return id;
    }

    public PlayerSummary getPlayerSummary() {
        return playerSummary;
    }

    public static List<PlayerEntity> fromPlayerSummaries(final List<PlayerSummary> playerSummaries) {
        final List<PlayerEntity> entities = new ArrayList<>();
        for (final PlayerSummary playerSummary : playerSummaries) {
            entities.add(new PlayerEntity(playerSummary));
        }
        return Collections.unmodifiableList(entities);
    }

    public static List<PlayerSummary> fromPlayerEntities(final List<PlayerEntity> entities) {
        final List<PlayerSummary> summaries = new ArrayList<>();
        for (final PlayerEntity entity : entities) {
            summaries.add(entity.getPlayerSummary());
        }
        return summaries;
    }

}
