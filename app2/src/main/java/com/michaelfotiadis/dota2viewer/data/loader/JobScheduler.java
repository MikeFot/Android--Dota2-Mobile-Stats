package com.michaelfotiadis.dota2viewer.data.loader;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.jobs.JobFactory;

public class JobScheduler {

    private final JobPriorityQueue mJobPriorityQueue;
    private final JobFactory mFactory;

    public JobScheduler(@NonNull final JobPriorityQueue jobPriorityQueue,
                        @NonNull final JobFactory factory) {
        mJobPriorityQueue = jobPriorityQueue;
        mFactory = factory;
    }

    public synchronized void clear() {
        mJobPriorityQueue.clear();
    }

    public void startFetchPlayersJob(@NonNull final String username) {
        mJobPriorityQueue.queueJob(mFactory.getFetchPlayersJob(username));
    }

    public void startFetchLibraryJob(@NonNull final String steamId) {
        mJobPriorityQueue.queueJob(mFactory.getFetchLibraryJob(steamId));
    }

    public void startFetchDotaItemsJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaItemsJob());
    }

    public void startFetchDotaHeroesJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaHeroesJob());
    }

    public void startFetchDotaRaritiesJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaRaritiesJob());
    }

    public void startFetchHeroDetailsJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaHeroDetailsJob());
    }

    public void startFetchDotaMatchOverviewsJob(@NonNull final String steamId3,
                                                @Nullable final Long startAtMatchId,
                                                final int requestedMatches,
                                                final boolean isAlsoFetchDetails,
                                                final boolean isAlsoFetchMetadata) {
        mJobPriorityQueue.queueJob(
                mFactory.getFetchDotaMatchOverviewsJob(
                        steamId3,
                        startAtMatchId,
                        requestedMatches,
                        isAlsoFetchDetails,
                        isAlsoFetchMetadata,
                        this)
        );
    }

    public void startFetchDotaMatchDetailsJob(final long matchId) {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaMatchDetailsJob(matchId));
    }

    public void startFetchHeroAttributesJob(final Resources resources) {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaHeroPatchAttrsJob(resources));
    }

    public void startFetchDotaLeagueListingsJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaLeagueListingsJob());
    }

    public void startFetchDotaLiveGamesJob() {
        mJobPriorityQueue.queueJob(mFactory.getFetchDotaLiveGamesJob());
    }

    public void startDeleteProfileJob(final String steamId64) {
        mJobPriorityQueue.queueJob(mFactory.getDeleteProfileJob(steamId64));
    }

}
