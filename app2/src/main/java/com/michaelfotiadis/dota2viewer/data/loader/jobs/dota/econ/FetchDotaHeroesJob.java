package com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.DotaHeroDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.event.dota.econ.FetchedDotaHeroesEvent;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.hero.Heroes;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2EconApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDotaHeroesJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.DAYS.toMillis(1);

    private final Dota2EconApiProvider mDota2EconApiProvider;
    private final DotaHeroDao mDao;
    private final DataPreferences mDataPreferences;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaHeroesJob(final Dota2EconApiProvider dota2EconApiProvider,
                              final DotaHeroDao dao,
                              final DataPreferences dataPreferences,
                              final NetworkResolver networkResolver) {
        mDota2EconApiProvider = dota2EconApiProvider;
        mDao = dao;
        mDataPreferences = dataPreferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaHeroesJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        if (System.currentTimeMillis() - mDataPreferences.getDotaHeroesUpdated() < UPDATE_THRESHOLD) {
            final List<DotaHeroEntity> heroes = mDao.getAllSync();
            if (heroes != null && !heroes.isEmpty()) {
                AppLog.d("Hero data is valid, no reason to run network call");
                postEvent(new FetchedDotaHeroesEvent(DotaHeroEntity.fromEntities(heroes)));
                postJobFinished();
            } else {
                fetchDataFromNet();
            }
        } else {
            fetchDataFromNet();

        }

    }

    private void fetchDataFromNet() {


        if (!mNetworkResolver.isConnected()) {
            AppLog.d("No internet connection for job " + getClass().getSimpleName());
            postError(new Error(ErrorKind.NO_NETWORK));
            return;
        }

        mDota2EconApiProvider.getHeroes(
                DEFAULT_LANGUAGE,
                false,
                new SteamCallback<ResultContainer<Heroes>>() {
                    @Override
                    public void onSuccess(final ResultContainer<Heroes> result) {
                        if (result != null && result.getResult() != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // we need to move back to a BG thread as Retrofit 2 callback is on Main
                                    AppLog.d("Received count= " + result.getResult().getCount() + " heroes");
                                    mDao.insert(DotaHeroEntity.fromHeroes(result.getResult().getHeroes()));
                                    mDataPreferences.writeDotaHeroesUpdated(System.currentTimeMillis());
                                    postEvent(new FetchedDotaHeroesEvent(result.getResult().getHeroes()));
                                    postJobFinished();

                                }
                            }).start();
                        } else {
                            postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
                        }
                    }

                    @Override
                    public void onError(final Reason reason, final int httpStatus) {
                        postError(Reason.UNKNOWN, httpStatus);
                    }
                });
    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }

    private void postError(final Error error) {
        mDataPreferences.writeDotaHeroesUpdated(Long.MIN_VALUE);
        postEvent(new FetchedDotaHeroesEvent(Collections.<Hero>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
