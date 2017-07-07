package com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaItemDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaItemEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaItemsEvent;
import com.michaelfotiadis.mobiledota2.network.NetworkResolver;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItems;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2EconApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDotaItemsJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.DAYS.toMillis(1);

    private final Dota2EconApiProvider mDota2EconApiProvider;
    private final DotaItemDao mDao;
    private final DataPreferences mDataPreferences;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaItemsJob(final Dota2EconApiProvider dota2EconApiProvider,
                             final DotaItemDao dao,
                             final DataPreferences dataPreferences,
                             final NetworkResolver networkResolver) {
        mDota2EconApiProvider = dota2EconApiProvider;
        mDao = dao;
        mDataPreferences = dataPreferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaItemsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        if (System.currentTimeMillis() - mDataPreferences.getDotaItemsUpdated() < UPDATE_THRESHOLD) {
            final List<DotaItemEntity> items = mDao.getAllSync();
            if (items != null && !items.isEmpty()) {
                AppLog.d("Item data is valid, no reason to run network call");
                postEvent(new FetchedDotaItemsEvent(DotaItemEntity.fromEntities(items)));
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

        mDota2EconApiProvider.getItems(DEFAULT_LANGUAGE, new SteamCallback<ResultContainer<GameItems>>() {
            @Override
            public void onSuccess(final ResultContainer<GameItems> result) {

                if (result != null && result.getResult() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // we need to move back to a BG thread as Retrofit 2 callback is on Main
                            AppLog.d("Received " + result.getResult().getItems().size() + " items");
                            mDao.insert(DotaItemEntity.fromItems(result.getResult().getItems()));
                            mDataPreferences.writeDotaItemsUpdated(System.currentTimeMillis());
                            postEvent(new FetchedDotaItemsEvent(result.getResult().getItems()));
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
        mDataPreferences.writeDotaItemsUpdated(Long.MIN_VALUE);
        postEvent(new FetchedDotaItemsEvent(Collections.<GameItem>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
