package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

public class MatchOverviewItem implements MatchItem {


    public MatchOverviewItem(@NonNull final MatchOverview matchOverview) {
        mMatchOverview = matchOverview;
    }

    private final MatchOverview mMatchOverview;

    @NonNull
    public MatchOverview getMatchOverview() {
        return mMatchOverview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeSerializable(this.mMatchOverview);
    }

    protected MatchOverviewItem(final Parcel in) {
        this.mMatchOverview = (MatchOverview) in.readSerializable();
    }

    public static final Creator<MatchOverviewItem> CREATOR = new Creator<MatchOverviewItem>() {
        @Override
        public MatchOverviewItem createFromParcel(final Parcel source) {
            return new MatchOverviewItem(source);
        }

        @Override
        public MatchOverviewItem[] newArray(final int size) {
            return new MatchOverviewItem[size];
        }
    };

    @Override
    public long getItemId() {
        return mMatchOverview.getMatchId();
    }
}

