package com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.league;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.DotaLeagueDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaLeagueEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.event.dota.league.FetchedLeagueListingsEvent;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.leagues.Leagues;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2MatchApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDotaLeagueListingsJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.DAYS.toMillis(1);

    private final Dota2MatchApiProvider mApi;
    private final DotaLeagueDao mDao;
    private final DataPreferences mPreferences;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaLeagueListingsJob(final Dota2MatchApiProvider api,
                                      final DotaLeagueDao dao,
                                      final DataPreferences preferences,
                                      final NetworkResolver networkResolver) {
        mApi = api;
        mDao = dao;
        mPreferences = preferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaLeagueListingsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        if (System.currentTimeMillis() - mPreferences.getLeaguesUpdated() < UPDATE_THRESHOLD) {
            final List<DotaLeagueEntity> leagues = mDao.getAllSync();
            if (leagues != null && !leagues.isEmpty()) {
                AppLog.d("League Listings data is valid, no reason to run network call");
                postEvent(new FetchedLeagueListingsEvent(DotaLeagueEntity.fromEntities(leagues)));
                postJobFinished();
            } else {
                fetchDataFromNet();
            }
        } else {
            fetchDataFromNet();
        }

    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }

    private void fetchDataFromNet() {

        if (!mNetworkResolver.isConnected()) {
            AppLog.d("No internet connection for job " + getClass().getSimpleName());
            postError(new Error(ErrorKind.NO_NETWORK));
            return;
        }

        mApi.getLeagueListing(DEFAULT_LANGUAGE, new SteamCallback<ResultContainer<Leagues>>() {
            @Override
            public void onSuccess(final ResultContainer<Leagues> result) {

                if (result != null && result.getResult() != null && result.getResult().getLeagues() != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // we need to move back to a BG thread as Retrofit 2 callback is on Main
                            mDao.insert(DotaLeagueEntity.fromModel(result.getResult().getLeagues()));
                            mPreferences.writeLeaguesUpdated(System.currentTimeMillis());
                            postEvent(new FetchedLeagueListingsEvent(result.getResult().getLeagues()));
                            postJobFinished();

                        }
                    }).start();

                } else {
                    postError(new Error(ErrorKind.INVALID_CONTENT));
                }

            }

            @Override
            public void onError(final Reason reason, final int httpStatus) {
                postError(reason, httpStatus);
            }
        });

    }

    private void postError(final Error error) {
        mPreferences.writeLeaguesUpdated(Long.MIN_VALUE);
        postEvent(new FetchedLeagueListingsEvent(Collections.<League>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
