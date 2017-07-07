package com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.match;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.BuildConfig;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaItemEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchDetailsEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchOverviewEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaHeroesEvent;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaItemsEvent;
import com.michaelfotiadis.mobiledota2.event.dota.match.FetchedDotaMatchDetailsEvent;
import com.michaelfotiadis.mobiledota2.event.dota.match.FetchedDotaMatchOverviewsEvent;
import com.michaelfotiadis.mobiledota2.network.NetworkResolver;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.match.history.MatchHistory;
import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2MatchApiProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDotaMatchOverviewsJob extends BaseJob {


    private static final long UPDATE_THRESHOLD = TimeUnit.HOURS.toMillis(1);

    private final String mSteamId3;
    @Nullable
    private final Long mStartAtMatchId;
    private final int mRequestedMatches;
    private final boolean mIsAlsoFetchDetails;
    private final boolean mIsAlsoFetchMetadata;
    private final Dota2MatchApiProvider mApi;
    private final AppDatabase mDb;
    private final DataPreferences mDataPreferences;
    private final JobScheduler mJobScheduler;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaMatchOverviewsJob(@NonNull final String steamId3,
                                      @Nullable final Long startAtMatchId,
                                      final int requestedMatches,
                                      final boolean isAlsoFetchDetails,
                                      final boolean isAlsoFetchMetadata,
                                      @NonNull final Dota2MatchApiProvider api,
                                      @NonNull final AppDatabase db,
                                      @NonNull final DataPreferences dataPreferences,
                                      @NonNull final JobScheduler jobScheduler,
                                      final NetworkResolver networkResolver) {

        mSteamId3 = steamId3;
        mStartAtMatchId = startAtMatchId;
        mRequestedMatches = requestedMatches;
        mApi = api;
        mDb = db;
        mDataPreferences = dataPreferences;
        mIsAlsoFetchDetails = isAlsoFetchDetails;
        mIsAlsoFetchMetadata = isAlsoFetchMetadata;
        mJobScheduler = jobScheduler;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaMatchOverviewsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {


        if (mIsAlsoFetchMetadata) {
            if (System.currentTimeMillis() - mDataPreferences.getDotaHeroesUpdated() < UPDATE_THRESHOLD) {
                final List<DotaHeroEntity> heroes = mDb.getDotaHeroDao().getAllSync();
                if (heroes != null && !heroes.isEmpty()) {
                    AppLog.d("Hero data is valid, no reason to run network call");
                    postEvent(new FetchedDotaHeroesEvent(DotaHeroEntity.fromEntities(heroes)));
                } else {
                    mJobScheduler.startFetchDotaHeroesJob();
                }
            } else {
                mJobScheduler.startFetchDotaHeroesJob();
            }
            if (System.currentTimeMillis() - mDataPreferences.getDotaItemsUpdated() < UPDATE_THRESHOLD) {
                final List<DotaItemEntity> items = mDb.getDotaItemDao().getAllSync();
                if (items != null && !items.isEmpty()) {
                    AppLog.d("Item data is valid, no reason to run network call");
                    postEvent(new FetchedDotaItemsEvent(DotaItemEntity.fromEntities(items)));
                } else {
                    mJobScheduler.startFetchDotaItemsJob();
                }
            } else {
                mJobScheduler.startFetchDotaItemsJob();
            }
        }

        if (System.currentTimeMillis() - mDataPreferences.getMatchOverviewUpdated(mSteamId3) < UPDATE_THRESHOLD) {
            final List<DotaMatchOverviewEntity> items = mDb.getDotaMatchOverviewDao().getByUserIdSync(mSteamId3);
            if (mStartAtMatchId == null && items != null && !items.isEmpty() && items.size() >= mRequestedMatches) {
                AppLog.d("Match Overview Entity data is valid, no reason to run network call");
                onOverviewsFetched();
            } else {
                AppLog.d("Match Overview Entity data is invalid, going for a network call");
                fetchDataFromNet(mStartAtMatchId);
            }
        } else {
            AppLog.d("Match Overview Entity data stale or not available - going for network call");
            fetchDataFromNet(mStartAtMatchId);

        }


    }

    private void fetchDataFromNet(@Nullable final Long startAtMatchId) {

        if (!mNetworkResolver.isConnected()) {
            AppLog.d("No internet connection for job " + getClass().getSimpleName());
            postError(new Error(ErrorKind.NO_NETWORK));
            return;
        }

        final List<DotaMatchOverviewEntity> existingItems = mDb.getDotaMatchOverviewDao().getByUserIdSync(mSteamId3);

        mApi.getMatchHistory(
                mSteamId3,
                mRequestedMatches,
                String.valueOf(startAtMatchId),
                new SteamCallback<ResultContainer<MatchHistory>>() {
                    @Override
                    public void onSuccess(final ResultContainer<MatchHistory> result) {

                        if (result != null && result.getResult() != null) {

                            if (result.getResult().getStatus() == 15) {
                                AppLog.d("Match Overview no permission");
                                postError(new Error(ErrorKind.NO_PERMISSION));
                            } else {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final List<MatchOverview> overviews = result.getResult().getMatches();
                                        // we need to move back to a BG thread as Retrofit 2 callback is on Main
                                        AppLog.d("Received " + overviews.size() + " matches");
                                        mDb.getDotaMatchOverviewDao().insert(DotaMatchOverviewEntity.fromMatches(mSteamId3, overviews));
                                        mDataPreferences.writeMatchOverviewUpdated(mSteamId3, System.currentTimeMillis());

                                        if (isThereDataOverlap(existingItems, overviews)) {
                                            onOverviewsFetched();
                                        } else {

                                            final Long nextMatchId = overviews.get(overviews.size() - 1).getMatchId();
                                            AppLog.d("Queuing next matches for sequence id " + nextMatchId);
                                            fetchDataFromNet(nextMatchId);
                                        }


                                    }
                                }).start();
                            }

                        }

                    }

                    @Override
                    public void onError(final Reason reason, final int httpStatus) {
                        AppLog.d("Match Overview Entity error with reason " + reason + " and status " + httpStatus);
                        postError(Reason.UNKNOWN, httpStatus);
                    }
                });
    }

    private void onOverviewsFetched() {

        final List<DotaMatchOverviewEntity> dbOverviews = mDb.getDotaMatchOverviewDao().getByUserIdSync(mSteamId3);

        if (dbOverviews == null) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("You messed up, boi");
            } else {
                AppLog.d("Match Overview error persisting");
                postError(new Error(ErrorKind.ERROR_PERSISTING));
            }
            return;
        }

        final List<MatchOverview> overviews = new ArrayList<>(DotaMatchOverviewEntity.toMatches(dbOverviews));

        Collections.sort(overviews, new Comparator<MatchOverview>() {
            @Override
            public int compare(final MatchOverview o1, final MatchOverview o2) {
                return (int) (o2.getStartTime()
                        - o1.getStartTime());
            }
        });

        final List<MatchOverview> result = Collections.unmodifiableList(overviews);

        postEvent(new FetchedDotaMatchOverviewsEvent(mSteamId3, result));
        postJobFinished();

        if (mIsAlsoFetchDetails) {

            for (final MatchOverview item : result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Long id = item.getMatchId();
                        final DotaMatchDetailsEntity detailsEntity = mDb.getDotaMatchDetailsDao().getByIdSync(id.toString());
                        // do not queue if it exists in DB - this data cannot be stale
                        if (detailsEntity != null) {
                            AppLog.d("Found match details in DB for id " + id);
                            postEvent(new FetchedDotaMatchDetailsEvent(detailsEntity.getDetails()));
                        } else {
                            AppLog.d("Will queue match details for id " + id);
                            mJobScheduler.startFetchDotaMatchDetailsJob(id);
                        }
                    }
                }).start();
            }
        }


    }

    private static boolean isThereDataOverlap(@Nullable final List<DotaMatchOverviewEntity> entities,
                                              @NonNull final List<MatchOverview> overviews) {

        if (entities == null || entities.isEmpty()) {
            AppLog.d("No existing entities, overlap is null by default");
            return true;
        } else {

            for (final MatchOverview overview : overviews) {

                for (final DotaMatchOverviewEntity entity : entities) {
                    if (entity.getOverview().getMatchId().equals(overview.getMatchId())) {
                        AppLog.d("Overlap found for id " + entity.getId());
                        return true;
                    }
                }
            }
        }
        AppLog.d("Overlap not found");
        return false;

    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }

    private void postError(final Error error) {
        mDataPreferences.writeMatchOverviewUpdated(mSteamId3, Long.MIN_VALUE);
        postEvent(new FetchedDotaMatchOverviewsEvent(mSteamId3, Collections.<MatchOverview>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
