package com.michaelfotiadis.mobiledota2.data.loader.jobs;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.econ.FetchDotaHeroesJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.econ.FetchDotaItemsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.econ.FetchDotaRaritiesJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.league.FetchDotaLeagueListingsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.league.FetchDotaLiveGamesJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.match.FetchDotaMatchDetailsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.match.FetchDotaMatchOverviewsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.stats.FetchDotaHeroDetailsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.dota.stats.FetchDotaHeroPatchAttrsJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.steam.DeleteProfileJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.steam.FetchLibraryJob;
import com.michaelfotiadis.mobiledota2.data.loader.jobs.steam.FetchPlayersJob;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.network.NetworkResolver;
import com.michaelfotiadis.mobiledota2.network.RestClient;
import com.michaelfotiadis.steam.SteamLoader;

import javax.inject.Inject;

public class JobFactory {

    @Inject
    SteamLoader mSteamLoader;
    @Inject
    RestClient mRestClient;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    DataPreferences mDataPreferences;
    @Inject
    UserPreferences mUserPreferences;
    @Inject
    NetworkResolver mNetworkResolver;


    public JobFactory() {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
    }

    public FetchPlayersJob getFetchPlayersJob(@NonNull final String username,
                                              final boolean storeToDb) {
        return new FetchPlayersJob(
                username,
                storeToDb,
                mSteamLoader.getUsersProvider(),
                mAppDatabase.getPlayerDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchLibraryJob getFetchLibraryJob(@NonNull final String steamId) {

        return new FetchLibraryJob(
                steamId,
                mSteamLoader.getPlayerProvider(),
                mAppDatabase.getLibraryDao(),
                mDataPreferences,
                mNetworkResolver
        );

    }

    public FetchDotaItemsJob getFetchDotaItemsJob() {
        return new FetchDotaItemsJob(
                mSteamLoader.getDota2EconomyApiProvider(),
                mAppDatabase.getDotaItemDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchDotaHeroesJob getFetchDotaHeroesJob() {
        return new FetchDotaHeroesJob(
                mSteamLoader.getDota2EconomyApiProvider(),
                mAppDatabase.getDotaHeroDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchDotaRaritiesJob getFetchDotaRaritiesJob() {
        return new FetchDotaRaritiesJob(
                mSteamLoader.getDota2EconomyApiProvider(),
                mAppDatabase.getDotaRarityDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchDotaHeroDetailsJob getFetchDotaHeroDetailsJob() {
        return new FetchDotaHeroDetailsJob(
                mRestClient.getHeroStatsApi(),
                mAppDatabase.getHeroDetailsDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchDotaMatchOverviewsJob getFetchDotaMatchOverviewsJob(@NonNull final String steamId3,
                                                                    @Nullable final Long startAtMatchId,
                                                                    final int requestedMatches,
                                                                    final boolean isAlsoFetchDetails,
                                                                    final boolean isAlsoFetchMetadata,
                                                                    @NonNull final JobScheduler jobScheduler) {
        return new FetchDotaMatchOverviewsJob(
                steamId3,
                startAtMatchId,
                requestedMatches,
                isAlsoFetchDetails,
                isAlsoFetchMetadata,
                mSteamLoader.getDota2MatchProvider(),
                mAppDatabase,
                mDataPreferences,
                jobScheduler,
                mNetworkResolver);
    }

    public FetchDotaMatchDetailsJob getFetchDotaMatchDetailsJob(final long matchId) {
        return new FetchDotaMatchDetailsJob(
                matchId,
                mSteamLoader.getDota2MatchProvider(),
                mAppDatabase.getDotaMatchDetailsDao(),
                mNetworkResolver);
    }

    public FetchDotaHeroPatchAttrsJob getFetchDotaHeroPatchAttrsJob(final Resources resources) {
        return new FetchDotaHeroPatchAttrsJob(resources, mAppDatabase.getHeroPatchAttributesDao());
    }

    public FetchDotaLeagueListingsJob getFetchDotaLeagueListingsJob() {
        return new FetchDotaLeagueListingsJob(
                mSteamLoader.getDota2MatchProvider(),
                mAppDatabase.getDotaLeagueDao(),
                mDataPreferences,
                mNetworkResolver);
    }

    public FetchDotaLiveGamesJob getFetchDotaLiveGamesJob() {
        return new FetchDotaLiveGamesJob(mSteamLoader.getDota2MatchProvider(), mNetworkResolver);
    }

    public DeleteProfileJob getDeleteProfileJob(final String steamId64) {
        return new DeleteProfileJob(steamId64, mAppDatabase, mUserPreferences);
    }

}
