package com.michaelfotiadis.dota2viewer.data.loader.jobs.steam;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.LibraryDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.LibraryEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedLibraryEvent;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.ResponseContainer;
import com.michaelfotiadis.steam.data.steam.player.library.Game;
import com.michaelfotiadis.steam.data.steam.player.library.Library;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.player.PlayerApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchLibraryJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.MINUTES.toMillis(5);

    private final String mSteamId;
    private final PlayerApiProvider mApi;
    private final DataPreferences mDataPreferences;
    @NonNull
    private final NetworkResolver mNetworkResolver;
    private final LibraryDao mDao;

    public FetchLibraryJob(@NonNull final String steamId,
                           @NonNull final PlayerApiProvider api,
                           @NonNull final LibraryDao dao,
                           @NonNull final DataPreferences dataPreferences,
                           @NonNull final NetworkResolver networkResolver) {
        super();
        mSteamId = steamId;
        this.mApi = api;
        mDao = dao;
        mDataPreferences = dataPreferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchLibraryJob.class.getSimpleName() + " job added on " + System.currentTimeMillis() + " for id " + mSteamId);
    }

    @Override
    public void onRun() throws Throwable {

        if (System.currentTimeMillis() - mDataPreferences.getLibraryUpdated(mSteamId) < UPDATE_THRESHOLD) {

            final LibraryEntity libraryEntity = mDao.getByIdSync(mSteamId);
            if (libraryEntity == null) {
                AppLog.d("Library does not exist in DB");
                fetchDataFromNet();
            } else {
                AppLog.d("Library retrieved from DB");
                postResult(libraryEntity.getLibrary().getGames());
            }
        } else {
            AppLog.d("Stale library data - will fetch from net");
            fetchDataFromNet();
        }


    }

    private void fetchDataFromNet() {

        if (!mNetworkResolver.isConnected()) {
            AppLog.d("No internet connection for job " + getClass().getSimpleName());
            postError(new Error(ErrorKind.NO_NETWORK));
            return;
        }

        AppLog.d("Fetching library from net for id= " + mSteamId);
        mApi.getLibrary(mSteamId, true, true, new SteamCallback<ResponseContainer<Library>>() {
            @Override
            public void onSuccess(final ResponseContainer<Library> libraryResponse) {

                if (libraryResponse == null
                        || libraryResponse.getResponse() == null
                        || libraryResponse.getResponse().getGames() == null) {
                    postError(new Error(ErrorKind.NO_CONTENT_RETURNED, 0));
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDao.insert(new LibraryEntity(mSteamId, libraryResponse.getResponse()));
                            mDataPreferences.writeLibraryUpdated(mSteamId, System.currentTimeMillis());

                            final List<Game> result = Collections.unmodifiableList(libraryResponse.getResponse().getGames());

                            postResult(result);
                        }
                    }).start();
                }
            }

            @Override
            public void onError(final Reason reason, final int httpStatus) {
                postError(reason, httpStatus);
            }
        });

    }

    private void postResult(final List<Game> games) {
        postEvent(new FetchedLibraryEvent(games));
        postJobFinished();
    }

    private void postError(final Error error) {
        mDataPreferences.writeLibraryUpdated(mSteamId, Long.MIN_VALUE);
        postEvent(new FetchedLibraryEvent(Collections.<Game>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }


}
