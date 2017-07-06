package com.michaelfotiadis.dota2viewer.data.persistence.db.accessor;

import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;

public class DbAccessor {

    private final PlayerAccessor mPlayerAccessor;
    private final DotaHeroAccessor mDotaHeroAccessor;

    public DbAccessor(final AppDatabase db) {
        mPlayerAccessor = new PlayerAccessor(db);
        mDotaHeroAccessor = new DotaHeroAccessor(db);
    }

    public PlayerAccessor getPlayerAccessor() {
        return mPlayerAccessor;
    }

    public DotaHeroAccessor getDotaHeroAccessor() {
        return mDotaHeroAccessor;
    }
}
