package com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.league;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.event.dota.league.FetchedLiveLeagueEvent;
import com.michaelfotiadis.mobiledota2.network.NetworkResolver;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGames;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2MatchApiProvider;

import java.util.Collections;

public class FetchDotaLiveGamesJob extends BaseJob {


    private final Dota2MatchApiProvider mApi;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaLiveGamesJob(final Dota2MatchApiProvider api, final NetworkResolver networkResolver) {
        mApi = api;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaLiveGamesJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        fetchDataFromNet();
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

        mApi.getLiveLeagueGames(DEFAULT_LANGUAGE, new SteamCallback<ResultContainer<LiveGames>>() {
            @Override
            public void onSuccess(final ResultContainer<LiveGames> result) {
                if (result != null && result.getResult() != null && result.getResult().getGames() != null) {
                    postEvent(new FetchedLiveLeagueEvent(result.getResult().getGames()));
                    postJobFinished();
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
        postEvent(new FetchedLiveLeagueEvent(Collections.<LiveGame>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
