package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

public class MatchListItem implements MatchItem {

    private final MatchOverviewItem mMatchOverviewItem;
    private MatchDetailsItem mMatchDetailsItem;
    private MatchErrorItem mMatchErrorItem;

    public MatchListItem(@Nullable final MatchOverview matchOverviewItem) {
        mMatchOverviewItem = matchOverviewItem == null ? null : new MatchOverviewItem(matchOverviewItem);
    }

    @Override
    public long getItemId() {
        return mMatchOverviewItem == null ? 0 : mMatchOverviewItem.getItemId();
    }

    @Nullable
    public MatchOverviewItem getMatchOverviewItem() {
        return mMatchOverviewItem;
    }

    @Nullable
    public MatchDetailsItem getMatchDetailsItem() {
        return mMatchDetailsItem;
    }

    public void setMatchDetailsItem(final MatchDetailsItem matchDetailsItem) {
        mMatchDetailsItem = matchDetailsItem;
    }

    @Nullable
    public MatchErrorItem getMatchErrorItem() {
        return mMatchErrorItem;
    }

    public void setMatchErrorItem(final MatchErrorItem matchErrorItem) {
        mMatchErrorItem = matchErrorItem;
    }

    public boolean isLoadingItem() {
        return mMatchOverviewItem == null;
    }

    public boolean isOverviewItem() {
        return mMatchErrorItem == null && mMatchDetailsItem == null && mMatchOverviewItem != null;
    }

    public boolean isDetailsItem() {
        return mMatchDetailsItem != null;
    }

    public boolean isErrorItem() {
        return mMatchErrorItem != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.mMatchOverviewItem, flags);
        dest.writeParcelable(this.mMatchDetailsItem, flags);
        dest.writeParcelable(this.mMatchErrorItem, flags);
    }

    protected MatchListItem(final Parcel in) {
        this.mMatchOverviewItem = in.readParcelable(MatchOverviewItem.class.getClassLoader());
        this.mMatchDetailsItem = in.readParcelable(MatchDetailsItem.class.getClassLoader());
        this.mMatchErrorItem = in.readParcelable(MatchErrorItem.class.getClassLoader());
    }

    public static final Creator<MatchListItem> CREATOR = new Creator<MatchListItem>() {
        @Override
        public MatchListItem createFromParcel(final Parcel source) {
            return new MatchListItem(source);
        }

        @Override
        public MatchListItem[] newArray(final int size) {
            return new MatchListItem[size];
        }
    };
}
