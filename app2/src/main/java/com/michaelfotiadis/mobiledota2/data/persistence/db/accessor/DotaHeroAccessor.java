package com.michaelfotiadis.mobiledota2.data.persistence.db.accessor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.DbCallback;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

import java.util.Collections;
import java.util.List;

public class DotaHeroAccessor extends Accessor {

    protected DotaHeroAccessor(final AppDatabase db) {
        super(db);
    }

    public void insert(@NonNull final Hero hero) {
        this.insert(Collections.singletonList(hero), null);
    }

    public void insert(@NonNull final Hero hero,
                       @Nullable final DbCallback dbCallback) {
        this.insert(Collections.singletonList(hero), dbCallback);
    }

    public void insert(final List<Hero> heroes) {
        this.insert(heroes, null);
    }

    public List<DotaHeroEntity> getSync() {
        return getDb().getDotaHeroDao().getAllSync();
    }

    public synchronized void insert(@NonNull final List<Hero> heroes,
                                    @Nullable final DbCallback dbCallback) {
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getDb().getDotaHeroDao().insert(DotaHeroEntity.fromHeroes(heroes));
                    AppLog.d(heroes.size() + " heroes persisted");
                    if (dbCallback != null) {
                        dbCallback.onSuccess();
                    }
                }
            }).start();
        }
    }

}
