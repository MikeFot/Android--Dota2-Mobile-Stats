package com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.match;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaMatchDetailsDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchDetailsEntity;
import com.michaelfotiadis.mobiledota2.event.dota.match.FetchedDotaMatchDetailsEvent;
import com.michaelfotiadis.mobiledota2.network.NetworkResolver;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.ResultContainer;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.dota.Dota2MatchApiProvider;

public class FetchDotaMatchDetailsJob extends BaseJob {

    private final long mMatchId;
    private final Dota2MatchApiProvider mApi;
    private final DotaMatchDetailsDao mDao;
    private final NetworkResolver mNetworkResolver;

    public FetchDotaMatchDetailsJob(final long matchId,
                                    final Dota2MatchApiProvider api,
                                    final DotaMatchDetailsDao dao,
                                    final NetworkResolver networkResolver) {
        mMatchId = matchId;
        mApi = api;
        mDao = dao;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaMatchDetailsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis() + " for id " + mMatchId);
    }

    @Override
    public void onRun() throws Throwable {


        final DotaMatchDetailsEntity entity = mDao.getByIdSync(String.valueOf(mMatchId));

        if (entity != null) {
            AppLog.d("Match details with ID " + mMatchId + " exists in DB");
            postEvent(new FetchedDotaMatchDetailsEvent(entity.getDetails()));
            postJobFinished();
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

        mApi.getMatchDetails(String.valueOf(mMatchId), new SteamCallback<ResultContainer<MatchDetails>>() {
            @Override
            public void onSuccess(final ResultContainer<MatchDetails> result) {

                if (result != null && result.getResult() != null) {

                    final MatchDetails details = result.getResult();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // we need to move back to a BG thread as Retrofit 2 callback is on Main

                            final DotaMatchDetailsEntity entity = new DotaMatchDetailsEntity(details);
                            mDao.insert(entity);
                            AppLog.d("Persisting new match details to DB: " + details.getMatchId());
                            postEvent(new FetchedDotaMatchDetailsEvent(details));
                            postJobFinished();

                        }
                    }).start();


                } else {
                    postError(new Error(ErrorKind.NO_CONTENT_RETURNED, 0));
                }

            }

            @Override
            public void onError(final Reason reason, final int httpStatus) {
                postError(reason, httpStatus);
            }
        });
    }

    private void postError(final Error error) {

        postEvent(new FetchedDotaMatchDetailsEvent(mMatchId, null, error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        this.postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

}
