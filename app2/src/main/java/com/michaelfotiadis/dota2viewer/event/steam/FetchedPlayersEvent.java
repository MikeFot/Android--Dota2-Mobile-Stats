package com.michaelfotiadis.dota2viewer.event.steam;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.event.Event;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.List;

public class FetchedPlayersEvent implements Event {

    private final List<PlayerSummary> mPlayers;
    private final Error mError;

    public FetchedPlayersEvent(final List<PlayerSummary> players, final Error error) {
        mPlayers = players;
        mError = error;
    }

    public FetchedPlayersEvent(final List<PlayerSummary> players) {
        mPlayers = players;
        mError = null;

    }

    public List<PlayerSummary> getPlayers() {
        return mPlayers;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

    @Override
    public String toString() {
        return "FetchedPlayersEvent{" +
                "mPlayers=" + mPlayers +
                ", mError=" + mError +
                '}';
    }
}
