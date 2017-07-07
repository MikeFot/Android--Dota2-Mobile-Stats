package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.DotaRarityConverter;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaRarityEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(DotaRarityConverter.class)
    private Rarity rarity;

    public DotaRarityEntity(@NonNull final Rarity rarity) {

        this.id = String.valueOf(rarity.getId());
        this.rarity = rarity;

    }

    public String getId() {
        return id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public static List<Rarity> fromEntities(final List<DotaRarityEntity> entities) {

        final List<Rarity> items = new ArrayList<>();

        for (final DotaRarityEntity entity : entities) {
            items.add(entity.getRarity());
        }

        return Collections.unmodifiableList(items);
    }

    public static List<DotaRarityEntity> fromItems(final List<Rarity> heroes) {
        final List<DotaRarityEntity> entities = new ArrayList<>();
        for (final Rarity item : heroes) {
            entities.add(new DotaRarityEntity(item));
        }
        return Collections.unmodifiableList(entities);
    }

}
