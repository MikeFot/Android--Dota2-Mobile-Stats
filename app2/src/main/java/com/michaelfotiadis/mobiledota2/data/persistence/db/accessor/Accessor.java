package com.michaelfotiadis.mobiledota2.data.persistence.db.accessor;

import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;

public abstract class Accessor {

    private final AppDatabase mDb;


    protected Accessor(final AppDatabase db) {
        mDb = db;

    }

    protected AppDatabase getDb() {
        return mDb;
    }
}
