package com.michaelfotiadis.mobiledota2.data.persistence.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.data.persistence.db.converter.LibraryConverter;
import com.michaelfotiadis.steam.data.steam.player.library.Library;

@SuppressWarnings("FieldMayBeFinal")
@Entity
public class LibraryEntity {

    @PrimaryKey()
    public String id;
    @TypeConverters(LibraryConverter.class)
    private Library library;

    public LibraryEntity(@NonNull final String id,
                         @NonNull final Library library) {

        this.id = id;
        this.library = library;

    }

    public String getId() {
        return id;
    }

    public Library getLibrary() {
        return library;
    }

}
