package com.michaelfotiadis.dota2viewer.data.loader.jobs.steam;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.error.LoaderErrorUtils;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.PlayerDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.ResponseContainer;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummaries;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;
import com.michaelfotiadis.steam.data.steam.users.vanity.Vanity;
import com.michaelfotiadis.steam.net.callback.Reason;
import com.michaelfotiadis.steam.provider.SteamCallback;
import com.michaelfotiadis.steam.provider.player.UsersApiProvider;
import com.michaelfotiadis.steam.utils.SteamIdUtils;
import com.michaelfotiadis.validator.annotated.validators.text.TextNumericValidatorHelper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchPlayersJob extends BaseJob {

    private static final long UPDATE_THRESHOLD = TimeUnit.MINUTES.toMillis(5);

    private final String mUsername;
    private final boolean mStoreItToDb;
    private final UsersApiProvider mApi;
    private final DataPreferences mDataPreferences;
    private final NetworkResolver mNetworkResolver;
    private final PlayerDao mDao;

    public FetchPlayersJob(@NonNull final String username,
                           final boolean isStore,
                           @NonNull final UsersApiProvider api,
                           @NonNull final PlayerDao dao,
                           @NonNull final DataPreferences dataPreferences,
                           final NetworkResolver networkResolver) {
        super();
        mUsername = username;
        mStoreItToDb = isStore;
        mApi = api;
        mDao = dao;
        mDataPreferences = dataPreferences;
        mNetworkResolver = networkResolver;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchPlayersJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        if (!mNetworkResolver.isConnected()) {
            AppLog.d("No internet connection for job " + getClass().getSimpleName());
            postError(new Error(ErrorKind.NO_NETWORK));
            return;
        }

        if (TextNumericValidatorHelper.isNumeric(mUsername)) {

            final Long id64 = convertUsernameToId64();

            if (id64 != null) {
                AppLog.d("Valid id64= " + id64);
                if (System.currentTimeMillis() - mDataPreferences.getProfileUpdated(id64.toString()) < UPDATE_THRESHOLD) {
                    final PlayerEntity entity = mDao.getByIdSync(id64.toString());
                    if (entity != null) {
                        AppLog.d("Found entity for id " + id64 + " in db");
                        final List<PlayerSummary> summaries = PlayerEntity.fromPlayerEntities(Collections.singletonList(entity));
                        postResult(summaries);
                    } else {
                        AppLog.d("Entity for id " + id64 + " does not exist in db");
                        fetchUserFromId64(id64);
                    }
                } else {
                    AppLog.d("Entity for id " + id64 + " is stale");
                    fetchUserFromId64(id64);
                }
            } else {
                AppLog.d("Invalid id64 for username " + mUsername);
                postError(new Error(ErrorKind.INVALID_REQUEST_PARAMETERS));
            }

        } else {
            AppLog.d("ID is not numeric, getting vanity URL");
            fetchIdFromUsername(mUsername);
        }

    }

    private void fetchIdFromUsername(final String username) {

        mApi.getIdFromVanityUrl(
                username,
                new SteamCallback<ResponseContainer<Vanity>>() {
                    @Override
                    public void onSuccess(final ResponseContainer<Vanity> result) {

                        if (result != null && result.getResponse() != null
                                && !TextUtils.isEmpty(result.getResponse().getSteamId())) {

                            final String id = result.getResponse().getSteamId();
                            if (TextNumericValidatorHelper.isNumeric(id) && SteamIdUtils.isSteamId64(Long.parseLong(id))) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fetchUserFromId64(Long.valueOf(id));
                                    }
                                }).start();
                                return;
                            }

                        }
                        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
                    }

                    @Override
                    public void onError(final Reason reason, final int httpStatus) {
                        postError(Reason.UNKNOWN, httpStatus);
                    }
                });

    }

    private void fetchUserFromId64(@NonNull final Long id64) {

        mApi.getPlayerSummaries(
                new String[]{id64.toString()},
                new SteamCallback<ResponseContainer<PlayerSummaries>>() {
                    @Override
                    public void onSuccess(final ResponseContainer<PlayerSummaries> result) {
                        AppLog.d(result.getResponse().getPlayers().size() + " results fetched");
                        if (result.getResponse() != null) {
                            if (mStoreItToDb) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // we need to move back to a BG thread as Retrofit 2 callback is on Main
                                        final List<PlayerEntity> entities = PlayerEntity.fromPlayerSummaries(result.getResponse().getPlayers());
                                        mDao.insert(entities);
                                        mDataPreferences.writeProfileUpdated(id64.toString(), System.currentTimeMillis());

                                    }
                                }).start();
                            } else {
                                postResult(result.getResponse().getPlayers());
                            }

                        } else {
                            postError(new Error(ErrorKind.INVALID_CONTENT));
                        }
                    }

                    @Override
                    public void onError(final Reason reason, final int httpStatus) {
                        mDataPreferences.writeProfileUpdated(id64.toString(), Long.MIN_VALUE);
                        postError(reason, httpStatus);
                    }
                });

    }

    private void postResult(final List<PlayerSummary> playerSummaries) {
        postEvent(new FetchedPlayersEvent(playerSummaries));
        postJobFinished();
    }

    private void postError(final Error error) {
        postEvent(new FetchedPlayersEvent(Collections.<PlayerSummary>emptyList(), error));
        postJobFinished();
    }

    private void postError(final Reason reason, final int httpStatus) {
        postError(LoaderErrorUtils.getError(reason, httpStatus));
    }

    @Nullable
    private Long convertUsernameToId64() {

        AppLog.d("This is probably a steam id");
        try {
            final Long idX = Long.parseLong(mUsername);

            if (SteamIdUtils.isSteamId64(idX)) {
                return idX;
            } else {
                final long id64 = SteamIdUtils.steamId3toSteam64(idX);
                return SteamIdUtils.isSteamId64(id64) ? id64 : null;
            }

        } catch (final Exception e) {
            return null;
        }

    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }


}
