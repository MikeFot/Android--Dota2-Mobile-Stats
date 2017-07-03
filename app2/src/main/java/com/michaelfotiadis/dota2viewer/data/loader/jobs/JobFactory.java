package com.michaelfotiadis.dota2viewer.data.loader.jobs;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.econ.FetchDotaHeroesJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.econ.FetchDotaItemsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.econ.FetchDotaRaritiesJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.league.FetchDotaLeagueListingsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.league.FetchDotaLiveGamesJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.match.FetchDotaMatchDetailsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.match.FetchDotaMatchOverviewsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.stats.FetchDotaHeroDetailsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.stats.FetchDotaHeroPatchAttrsJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.steam.DeleteProfileJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.steam.FetchLibraryJob;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.steam.FetchPlayersJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.DataPreferences;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.network.NetworkResolver;
import com.michaelfotiadis.dota2viewer.network.RestClient;
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

    public FetchPlayersJob getFetchPlayersJob(@NonNull final String username) {
        return new FetchPlayersJob(
                username,
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
