package com.michaelfotiadis.dota2viewer.data.persistence.model;

/**
 */
public class HeroStatistics {
    private final int mHeroId;
    private final String mHeroName;
    private final String mLocalisedName;
    private final int mPrimaryStat;
    private final int mTimesPlayed;
    private final int mTimesWon;
    private final int mTotalKills;
    private final int mTotalAssists;
    private final int mTotalDeaths;
    private final int mTotalGpM;
    private final int mTotalXpM;
    private final int mTotalLastHits;
    private final int mTotalDenies;

    private HeroStatistics(final Builder builder) {
        mHeroId = builder.mHeroId;
        mHeroName = builder.mHeroName;
        mLocalisedName = builder.mLocalisedName;
        mTimesPlayed = builder.mTimesPlayed;
        mTimesWon = builder.mTimesWon;
        mTotalKills = builder.mTotalKills;
        mTotalAssists = builder.mTotalAssists;
        mTotalDeaths = builder.mTotalDeaths;
        mTotalGpM = builder.mTotalGpM;
        mTotalXpM = builder.mTotalXpM;
        mTotalLastHits = builder.mTotalLastHits;
        mTotalDenies = builder.mTotalDenies;
        mPrimaryStat = builder.mPrimaryStat;
    }


    public int getHeroId() {
        return mHeroId;
    }

    public String getHeroName() {
        return mHeroName;
    }

    public String getLocalisedName() {
        return mLocalisedName;
    }

    public int getTotalAssists() {
        return mTotalAssists;
    }

    public int getTimesPlayed() {
        return mTimesPlayed;
    }

    public int getTimesWon() {
        return mTimesWon;
    }

    public int getTotalKills() {
        return mTotalKills;
    }

    public int getTotalDeaths() {
        return mTotalDeaths;
    }

    public int getTotalGpM() {
        return mTotalGpM;
    }

    public int getTotalXpM() {
        return mTotalXpM;
    }

    public int getTotalLastHits() {
        return mTotalLastHits;
    }

    public int getTotalDenies() {
        return mTotalDenies;
    }

    public float getAverageWins() {
        return mTimesPlayed > 0 ? mTimesWon / (float) mTimesPlayed : 0f;
    }

    public float getAverageKills() {
        return mTimesPlayed > 0 ? mTotalKills / (float) mTimesPlayed : 0f;
    }

    public float getAverageDeaths() {
        return mTimesPlayed > 0 ? mTotalDeaths / (float) mTimesPlayed : 0f;
    }

    public float getAverageAssists() {
        return mTimesPlayed > 0 ? mTotalAssists / (float) mTimesPlayed : 0f;
    }

    public float getAverageGpM() {
        return mTimesPlayed > 0 ? mTotalGpM / (float) mTimesPlayed : 0f;
    }

    public float getAverageXpM() {
        return mTimesPlayed > 0 ? mTotalXpM / (float) mTimesPlayed : 0f;
    }

    public float getAverageLastHits() {
        return mTimesPlayed > 0 ? mTotalLastHits / (float) mTimesPlayed : 0f;
    }

    public float getAverageDenies() {
        return mTimesPlayed > 0 ? mTotalDenies / (float) mTimesPlayed : 0f;
    }

    public int getPrimaryStat() {
        return mPrimaryStat;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private int mHeroId;
        private String mHeroName;
        private String mLocalisedName;
        private int mTimesPlayed;
        private int mTimesWon;
        private int mTotalKills;
        private int mTotalAssists;
        private int mTotalDeaths;
        private int mTotalGpM;
        private int mTotalXpM;
        private int mTotalLastHits;
        private int mTotalDenies;
        public int mPrimaryStat;

        private Builder() {
        }

        public Builder withHeroId(final int val) {
            mHeroId = val;
            return this;
        }

        public Builder withHeroName(final String val) {
            mHeroName = val;
            return this;
        }

        public Builder withLocalisedName(final String val) {
            mLocalisedName = val;
            return this;
        }

        public Builder withTimesPlayed(final int val) {
            mTimesPlayed = val;
            return this;
        }

        public Builder withTimesWon(final int val) {
            mTimesWon = val;
            return this;
        }

        public Builder withTotalKills(final int val) {
            mTotalKills = val;
            return this;
        }

        public Builder withTotalAssists(final int val) {
            mTotalAssists = val;
            return this;
        }

        public Builder withTotalDeaths(final int val) {
            mTotalDeaths = val;
            return this;
        }

        public Builder withTotalGpM(final int val) {
            mTotalGpM = val;
            return this;
        }

        public Builder withTotalXpM(final int val) {
            mTotalXpM = val;
            return this;
        }

        public Builder withTotalLastHits(final int val) {
            mTotalLastHits = val;
            return this;
        }

        public Builder withTotalDenies(final int val) {
            mTotalDenies = val;
            return this;
        }

        public Builder withPrimaryStat(final int val) {
            mPrimaryStat = val;
            return this;
        }

        public HeroStatistics build() {
            return new HeroStatistics(this);
        }
    }
}
