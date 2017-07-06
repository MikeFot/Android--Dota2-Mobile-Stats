package com.michaelfotiadis.dota2viewer.event.dota.match;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

import java.util.List;

public class FetchedDotaMatchOverviewsEvent implements Event {

    private final String mId3;
    private final List<MatchOverview> mMatchOverviews;
    private final Error mError;

    public FetchedDotaMatchOverviewsEvent(final String id3, final List<MatchOverview> matchOverviews, final Error error) {
        mId3 = id3;
        mMatchOverviews = matchOverviews;
        mError = error;
    }

    public FetchedDotaMatchOverviewsEvent(final String id3, final List<MatchOverview> matchOverviews) {
        this(id3, matchOverviews, null);
    }

    public List<MatchOverview> getMatchOverviews() {
        return mMatchOverviews;
    }

    public String getId3() {
        return mId3;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

}
