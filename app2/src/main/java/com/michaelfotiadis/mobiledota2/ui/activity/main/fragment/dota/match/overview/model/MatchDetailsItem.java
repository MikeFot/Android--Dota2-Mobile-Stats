package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import java.util.ArrayList;
import java.util.List;

public class MatchDetailsItem implements MatchItem {

    private final long mPlayerId;
    private final MatchDetails mMatchDetails;
    private final List<Hero> mHeroes;
    private final List<GameItem> mItems;


    public MatchDetailsItem(final long playerId,
                            @NonNull final MatchDetails matchDetails,
                            @NonNull final List<Hero> heroes,
                            @NonNull final List<GameItem> items) {
        mPlayerId = playerId;
        mMatchDetails = matchDetails;
        mHeroes = heroes;
        mItems = items;
    }

    public long getPlayerId() {
        return mPlayerId;
    }

    public MatchDetails getMatchDetails() {
        return mMatchDetails;
    }

    @NonNull
    public List<Hero> getHeroes() {
        return mHeroes;
    }

    @NonNull
    public List<GameItem> getItems() {
        return mItems;
    }

    @Override
    public long getItemId() {
        return mMatchDetails.getMatchId();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.mPlayerId);
        dest.writeSerializable(this.mMatchDetails);
        dest.writeList(this.mHeroes);
        dest.writeList(this.mItems);
    }

    protected MatchDetailsItem(final Parcel in) {
        this.mPlayerId = in.readLong();
        this.mMatchDetails = (MatchDetails) in.readSerializable();
        this.mHeroes = new ArrayList<>();
        in.readList(this.mHeroes, Hero.class.getClassLoader());
        this.mItems = new ArrayList<>();
        in.readList(this.mItems, GameItem.class.getClassLoader());
    }

    public static final Creator<MatchDetailsItem> CREATOR = new Creator<MatchDetailsItem>() {
        @Override
        public MatchDetailsItem createFromParcel(final Parcel source) {
            return new MatchDetailsItem(source);
        }

        @Override
        public MatchDetailsItem[] newArray(final int size) {
            return new MatchDetailsItem[size];
        }
    };
}
