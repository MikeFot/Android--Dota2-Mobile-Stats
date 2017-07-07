package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.DotaMatchOverviewConverter;
import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaMatchOverviewEntity {

    @PrimaryKey()
    public String id;
    public String userId;
    @TypeConverters(DotaMatchOverviewConverter.class)
    private MatchOverview overview;

    public DotaMatchOverviewEntity(@NonNull String userId,
                                   @NonNull final MatchOverview overview) {

        this.id = String.valueOf(overview.getMatchId());
        this.userId = userId;
        this.overview = overview;

    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public MatchOverview getOverview() {
        return overview;
    }

    public static List<MatchOverview> toMatches(final List<DotaMatchOverviewEntity> entities) {

        final List<MatchOverview> overviews = new ArrayList<>();

        for (final DotaMatchOverviewEntity entity : entities) {
            overviews.add(entity.getOverview());
        }

        return Collections.unmodifiableList(overviews);
    }

    public static List<DotaMatchOverviewEntity> fromMatches(final String userId,
                                                            final List<MatchOverview> overviews) {
        final List<DotaMatchOverviewEntity> entities = new ArrayList<>();
        for (final MatchOverview overview : overviews) {
            entities.add(new DotaMatchOverviewEntity(userId, overview));
        }
        return Collections.unmodifiableList(entities);
    }

}
