package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.DotaMatchDetailsConverter;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaMatchDetailsEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(DotaMatchDetailsConverter.class)
    private MatchDetails details;

    public DotaMatchDetailsEntity(@NonNull final MatchDetails details) {

        this.id = String.valueOf(details.getMatchId());
        this.details = details;

    }

    public String getId() {
        return id;
    }

    public MatchDetails getDetails() {
        return details;
    }

    public static List<MatchDetails> fromEntities(final List<DotaMatchDetailsEntity> entities) {

        final List<MatchDetails> matches = new ArrayList<>();

        for (final DotaMatchDetailsEntity entity : entities) {
            matches.add(entity.getDetails());
        }

        return Collections.unmodifiableList(matches);
    }

    public static List<DotaMatchDetailsEntity> fromMatches(final List<MatchDetails> overviews) {
        final List<DotaMatchDetailsEntity> entities = new ArrayList<>();
        for (final MatchDetails details : overviews) {
            entities.add(new DotaMatchDetailsEntity(details));
        }
        return Collections.unmodifiableList(entities);
    }

}
