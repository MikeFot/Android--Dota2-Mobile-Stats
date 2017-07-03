package com.michaelfotiadis.dota2viewer.data.persistence.db.accessor;

import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;

public abstract class Accessor {

    private final AppDatabase mDb;


    protected Accessor(final AppDatabase db) {
        mDb = db;

    }

    protected AppDatabase getDb() {
        return mDb;
    }
}
