package com.michaelfotiadis.mobiledota2.event.dota.league;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

import java.util.List;

public class FetchedLeagueListingsEvent implements Event {

    private final List<League> mLeagues;
    private final Error mError;

    public FetchedLeagueListingsEvent(final List<League> leagues, final Error error) {
        AppLog.d("New Fetch League listings event created");
        mLeagues = leagues;
        mError = error;
    }

    public FetchedLeagueListingsEvent(final List<League> leagues) {
        this(leagues, null);
    }

    public List<League> getLeagues() {
        return mLeagues;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

}
