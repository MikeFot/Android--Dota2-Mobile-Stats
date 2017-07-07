package com.michaelfotiadis.mobiledota2.event.dota.league;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;

import java.util.List;

public class FetchedLiveLeagueEvent implements Event {

    private final List<LiveGame> mLiveGames;
    private final Error mError;

    public FetchedLiveLeagueEvent(final List<LiveGame> liveGames, final Error error) {
        mLiveGames = liveGames;
        mError = error;
    }

    public FetchedLiveLeagueEvent(final List<LiveGame> liveGames) {
        this(liveGames, null);
    }

    public List<LiveGame> getLiveGames() {
        return mLiveGames;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

}
