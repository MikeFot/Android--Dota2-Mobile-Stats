package com.michaelfotiadis.mobiledota2.event.dota.match;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

public class FetchedDotaMatchDetailsEvent implements Event {

    private final Long mId;
    private final MatchDetails mMatchDetails;
    private final Error mError;

    public FetchedDotaMatchDetailsEvent(final long id, final MatchDetails matchDetails, final Error error) {
        mId = id;
        mMatchDetails = matchDetails;
        mError = error;
    }

    public FetchedDotaMatchDetailsEvent(final MatchDetails matchDetails) {
        this(matchDetails.getMatchId(), matchDetails, null);
    }

    public Long getId() {
        return mId;
    }

    public MatchDetails getMatch() {
        return mMatchDetails;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

}
