package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.DotaHeroConverter;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaHeroEntity {

    @PrimaryKey()
    public String name;
    @TypeConverters(DotaHeroConverter.class)
    private Hero hero;

    public DotaHeroEntity(@NonNull final Hero hero) {

        this.name = String.valueOf(hero.getName());
        this.hero = hero;

    }

    public String getName() {
        return name;
    }

    public Hero getHero() {
        return hero;
    }

    public static List<Hero> fromEntities(final List<DotaHeroEntity> entities) {

        final List<Hero> heroes = new ArrayList<>();

        for (final DotaHeroEntity entity : entities) {
            heroes.add(entity.getHero());
        }

        return Collections.unmodifiableList(heroes);
    }

    public static List<DotaHeroEntity> fromHeroes(final List<Hero> heroes) {
        final List<DotaHeroEntity> entities = new ArrayList<>();
        for (final Hero hero : heroes) {
            entities.add(new DotaHeroEntity(hero));
        }
        return Collections.unmodifiableList(entities);
    }

}
