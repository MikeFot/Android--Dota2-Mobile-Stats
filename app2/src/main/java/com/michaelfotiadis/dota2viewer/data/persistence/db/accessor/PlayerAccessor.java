package com.michaelfotiadis.dota2viewer.data.persistence.db.accessor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.data.persistence.db.DbCallback;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.Collections;
import java.util.List;

public class PlayerAccessor extends Accessor {


    protected PlayerAccessor(final AppDatabase db) {
        super(db);
    }

    public void insert(@NonNull final PlayerSummary playerSummary) {
        this.insert(Collections.singletonList(playerSummary), null);
    }

    public void insert(@NonNull final PlayerSummary playerSummary,
                       @Nullable final DbCallback dbCallback) {
        this.insert(Collections.singletonList(playerSummary), dbCallback);
    }

    public void insert(final List<PlayerSummary> playerSummaries) {
        this.insert(playerSummaries, null);
    }

    public List<PlayerEntity> getSync() {
        return getDb().getPlayerDao().getAllSync();
    }

    public synchronized void insert(@NonNull final List<PlayerSummary> playerSummaries,
                                    @Nullable final DbCallback dbCallback) {
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getDb().getPlayerDao().insert(PlayerEntity.fromPlayerSummaries(playerSummaries));
                    AppLog.d(playerSummaries.size() + " player summaries persisted");
                    if (dbCallback != null) {
                        dbCallback.onSuccess();
                    }
                }
            }).start();
        }
    }

}
