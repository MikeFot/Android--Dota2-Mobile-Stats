package com.michaelfotiadis.mobiledota2.data.loader.jobs.steam;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;

import java.util.List;

public class DeleteProfileJob extends BaseJob {

    private final String mSteamId64;
    private final AppDatabase mAppDatabase;
    private final UserPreferences mUserPreferences;

    public DeleteProfileJob(final String steamId64,
                            final AppDatabase appDatabase,
                            final UserPreferences userPreferences) {
        super();
        mSteamId64 = steamId64;
        mAppDatabase = appDatabase;
        mUserPreferences = userPreferences;
    }

    @Override
    public void onAdded() {
        AppLog.d(DeleteProfileJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        mAppDatabase.getPlayerDao().delete(mSteamId64);

        final String currentId = mUserPreferences.getCurrentUserId();
        if (TextUtils.isNotEmpty(currentId)) {
            if (currentId.equals(mSteamId64)) {
                // we need to get rid of the current ID
                final List<PlayerEntity> remainingPlayers = mAppDatabase.getPlayerDao().getAllSync();
                final String nextId;
                if (remainingPlayers.isEmpty()) {
                    nextId = null;
                } else {
                    nextId = remainingPlayers.get(0) != null ? remainingPlayers.get(0).getId() : null;
                }
                AppLog.d("Switched to next User id: " + nextId);
                mUserPreferences.writeCurrentUserId(nextId);
            }
        }

        postJobFinished();

    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {

    }
}
