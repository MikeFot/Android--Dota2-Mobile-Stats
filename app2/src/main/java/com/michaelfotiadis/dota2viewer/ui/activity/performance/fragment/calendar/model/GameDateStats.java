package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class GameDateStats implements Parcelable {

    private final int mDayOfYear;
    private final int mYear;
    private final int mWins;
    private final int mLosses;
    private final int mSum;
    private final List<Boolean> mSequence;

    public GameDateStats(final int dayOfYear,
                         final int year,
                         final int mWins,
                         final int mLosses,
                         final List<Boolean> sequence) {
        this.mDayOfYear = dayOfYear;
        this.mYear = year;
        this.mWins = mWins;
        this.mLosses = mLosses;
        this.mSequence = sequence;
        this.mSum = mWins - mLosses;
    }

    public int getDayOfYear() {
        return mDayOfYear;
    }

    public int getWins() {
        return mWins;
    }

    public int getLosses() {
        return mLosses;
    }

    public int getSum() {
        return mSum;
    }

    public int getYear() {
        return mYear;
    }

    public List<Boolean> getSequence() {
        return mSequence;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.mDayOfYear);
        dest.writeInt(this.mYear);
        dest.writeInt(this.mWins);
        dest.writeInt(this.mLosses);
        dest.writeInt(this.mSum);
        dest.writeList(this.mSequence);
    }

    protected GameDateStats(final Parcel in) {
        this.mDayOfYear = in.readInt();
        this.mYear = in.readInt();
        this.mWins = in.readInt();
        this.mLosses = in.readInt();
        this.mSum = in.readInt();
        this.mSequence = new ArrayList<>();
        in.readList(this.mSequence, Boolean.class.getClassLoader());
    }

    public static final Creator<GameDateStats> CREATOR = new Creator<GameDateStats>() {
        @Override
        public GameDateStats createFromParcel(final Parcel source) {
            return new GameDateStats(source);
        }

        @Override
        public GameDateStats[] newArray(final int size) {
            return new GameDateStats[size];
        }
    };
}
