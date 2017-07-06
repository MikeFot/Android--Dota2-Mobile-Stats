package com.michaelfotiadis.dota2viewer.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.dota2viewer.data.persistence.db.converter.DotaItemConverter;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class DotaItemEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(DotaItemConverter.class)
    private GameItem item;

    public DotaItemEntity(@NonNull final GameItem item) {

        this.id = String.valueOf(item.getId());
        this.item = item;

    }

    public String getId() {
        return id;
    }

    public GameItem getItem() {
        return item;
    }

    public static List<GameItem> fromEntities(final List<DotaItemEntity> entities) {

        final List<GameItem> items = new ArrayList<>();

        for (final DotaItemEntity entity : entities) {
            items.add(entity.getItem());
        }

        return Collections.unmodifiableList(items);
    }

    public static List<DotaItemEntity> fromItems(final List<GameItem> items) {
        final List<DotaItemEntity> entities = new ArrayList<>();
        for (final GameItem item : items) {
            entities.add(new DotaItemEntity(item));
        }
        return Collections.unmodifiableList(entities);
    }

}
