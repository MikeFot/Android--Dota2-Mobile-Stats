package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.HeroPatchAttributesConverter;
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class HeroPatchAttributesEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(HeroPatchAttributesConverter.class)
    private HeroPatchAttributes hero;

    public HeroPatchAttributesEntity(@NonNull final HeroPatchAttributes hero) {

        this.id = hero.getHero();
        this.hero = hero;

    }

    public String getId() {
        return id;
    }

    public HeroPatchAttributes getHero() {
        return hero;
    }

    public static List<HeroPatchAttributes> fromEntities(final List<HeroPatchAttributesEntity> entities) {

        final List<HeroPatchAttributes> heroes = new ArrayList<>();

        for (final HeroPatchAttributesEntity entity : entities) {
            heroes.add(entity.getHero());
        }

        return Collections.unmodifiableList(heroes);
    }

    public static List<HeroPatchAttributesEntity> fromHeroes(final List<HeroPatchAttributes> heroes) {
        final List<HeroPatchAttributesEntity> entities = new ArrayList<>();
        for (final HeroPatchAttributes hero : heroes) {
            entities.add(new HeroPatchAttributesEntity(hero));
        }
        return Collections.unmodifiableList(entities);
    }

}
