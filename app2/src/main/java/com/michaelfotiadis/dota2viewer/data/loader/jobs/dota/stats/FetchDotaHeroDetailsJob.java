package com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.stats;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.HeroDetailsDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.HeroDetailsEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.event.dota.stats.FetchedDotaHeroDetailsEvent;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.network.api.HeroStatsApi;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;
import com.michaelfotiadis.steam.net.callback.NetworkCallback;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.net.callback.Retrofit2CallbackFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FetchDotaHeroDetailsJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.DAYS.toMillis(2);

    private final HeroStatsApi mApi;
    private final HeroDetailsDao mDao;
    private final DataPreferences mPreferences;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaHeroDetailsJob(final HeroStatsApi api,
                                   final HeroDetailsDao dao,
                                   final DataPreferences dataPreferences,
                                   final NetworkResolver networkResolver) {
        mApi = api;
        mDao = dao;
        mPreferences = dataPreferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaHeroDetailsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        if (System.currentTimeMillis() - mPreferences.getHeroDetailsUpdated() < UPDATE_THRESHOLD) {
            final List<HeroDetailsEntity> heroes = mDao.getAllSync();
            if (heroes != null && !heroes.isEmpty()) {
                AppLog.d("Hero Details data is valid, no reason to run network call");
                postEvent(new FetchedDotaHeroDetailsEvent(HeroDetailsEntity.fromEntities(heroes)));
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

        mApi.getDetailsAllHeroes().enqueue(
                Retrofit2CallbackFactory.create(new NetworkCallback<Map<String, HeroDetails>>() {
                    @Override
                    public void onResponse(final String url, final Map<String, HeroDetails> payload, final boolean is2XX, final int httpStatus) {
                        if (is2XX && payload != null) {
                            final List<HeroDetails> heroDetails = new ArrayList<>();
                            heroDetails.addAll(payload.values());

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // we need to move back to a BG thread as Retrofit 2 callback is on Main
                                    AppLog.d("Received count= " + heroDetails.size() + " heroes");
                                    mDao.insert(HeroDetailsEntity.fromHeroes(heroDetails));
                                    mPreferences.writeHeroDetailsUpdated(System.currentTimeMillis());
                                    postEvent(new FetchedDotaHeroDetailsEvent(heroDetails));
                                    postJobFinished();

                                }
                            }).start();

                        } else {
                            postError(new Error(ErrorKind.NO_CONTENT_RETURNED, httpStatus));
                        }
                    }

                    @Override
                    public void onFailure(final String url, final Reason reason) {
                        postError(reason, 0);
                    }
                }));
    }

    private void postError(final Error error) {
        mPreferences.writeHeroDetailsUpdated(Long.MIN_VALUE);
        postEvent(new FetchedDotaHeroDetailsEvent(Collections.<HeroDetails>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
