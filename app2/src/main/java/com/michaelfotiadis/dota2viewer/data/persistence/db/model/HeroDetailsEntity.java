package com.michaelfotiadis.dota2viewer.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.dota2viewer.data.persistence.db.converter.HeroDetailsConverter;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class HeroDetailsEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(HeroDetailsConverter.class)
    private HeroDetails hero;

    public HeroDetailsEntity(@NonNull final HeroDetails hero) {

        this.id = hero.getName();
        this.hero = hero;

    }

    public String getId() {
        return id;
    }

    public HeroDetails getHero() {
        return hero;
    }

    public static List<HeroDetails> fromEntities(final List<HeroDetailsEntity> entities) {

        final List<HeroDetails> heroes = new ArrayList<>();

        for (final HeroDetailsEntity entity : entities) {
            heroes.add(entity.getHero());
        }

        return Collections.unmodifiableList(heroes);
    }

    public static List<HeroDetailsEntity> fromHeroes(final List<HeroDetails> heroes) {
        final List<HeroDetailsEntity> entities = new ArrayList<>();
        for (final HeroDetails hero : heroes) {
            entities.add(new HeroDetailsEntity(hero));
        }
        return Collections.unmodifiableList(entities);
    }

}
