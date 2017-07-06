package com.michaelfotiadis.dota2viewer.data.persistence.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.ArrayList;
import java.util.List;

public class Players implements Parcelable {

    private final List<PlayerSummary> mPlayers;

    public Players(final List<PlayerSummary> players) {
        mPlayers = players;
    }

    public List<PlayerSummary> getPlayers() {
        return mPlayers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeList(this.mPlayers);
    }

    protected Players(final Parcel in) {
        this.mPlayers = new ArrayList<>();
        in.readList(this.mPlayers, PlayerSummary.class.getClassLoader());
    }

    public static final Creator<Players> CREATOR = new Creator<Players>() {
        @Override
        public Players createFromParcel(final Parcel source) {
            return new Players(source);
        }

        @Override
        public Players[] newArray(final int size) {
            return new Players[size];
        }
    };
}
