package com.michaelfotiadis.dota2viewer.ui.activity.details;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import java.util.ArrayList;
import java.util.List;

public class MatchContainer implements Parcelable {

    private final long mUserId3;
    private final MatchDetails mMatchDetails;
    private final List<Hero> mHeroes;
    private final List<GameItem> mGameItems;

    public MatchContainer(final long userId3,
                          @NonNull final MatchDetails matchDetails,
                          @NonNull final List<Hero> heroes,
                          @NonNull final List<GameItem> gameItems) {
        mUserId3 = userId3;
        mMatchDetails = matchDetails;
        mHeroes = heroes;
        mGameItems = gameItems;
    }

    public long getUserId3() {
        return mUserId3;
    }

    public MatchDetails getMatchDetails() {
        return mMatchDetails;
    }

    public List<Hero> getHeroes() {
        return mHeroes;
    }

    public List<GameItem> getGameItems() {
        return mGameItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.mUserId3);
        dest.writeSerializable(this.mMatchDetails);
        dest.writeList(this.mHeroes);
        dest.writeList(this.mGameItems);
    }

    protected MatchContainer(final Parcel in) {
        this.mUserId3 = in.readLong();
        this.mMatchDetails = (MatchDetails) in.readSerializable();
        this.mHeroes = new ArrayList<>();
        in.readList(this.mHeroes, Hero.class.getClassLoader());
        this.mGameItems = new ArrayList<>();
        in.readList(this.mGameItems, GameItem.class.getClassLoader());
    }

    public static final Creator<MatchContainer> CREATOR = new Creator<MatchContainer>() {
        @Override
        public MatchContainer createFromParcel(final Parcel source) {
            return new MatchContainer(source);
        }

        @Override
        public MatchContainer[] newArray(final int size) {
            return new MatchContainer[size];
        }
    };
}
