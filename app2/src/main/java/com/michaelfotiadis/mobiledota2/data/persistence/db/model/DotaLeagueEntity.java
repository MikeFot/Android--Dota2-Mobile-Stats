package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.DotaLeagueConverter;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaLeagueEntity {

    @PrimaryKey()
    public Integer id;
    @TypeConverters(DotaLeagueConverter.class)
    private League league;

    public DotaLeagueEntity(@NonNull final League league) {

        this.id = league.getLeagueid();
        this.league = league;

    }

    public Integer getId() {
        return id;
    }

    public League getLeague() {
        return league;
    }

    public static List<League> fromEntities(final List<DotaLeagueEntity> entities) {

        final List<League> heroes = new ArrayList<>();

        for (final DotaLeagueEntity entity : entities) {
            heroes.add(entity.getLeague());
        }

        return Collections.unmodifiableList(heroes);
    }

    public static List<DotaLeagueEntity> fromModel(final List<League> leagues) {
        final List<DotaLeagueEntity> entities = new ArrayList<>();
        for (final League league : leagues) {
            entities.add(new DotaLeagueEntity(league));
        }
        return Collections.unmodifiableList(entities);
    }

}
