package com.michaelfotiadis.mobiledota2.event.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

import java.util.List;

public class FetchedDotaRaritiesEvent implements Event {

    private final List<Rarity> mRarities;
    private final Error mError;

    public FetchedDotaRaritiesEvent(final List<Rarity> rarities, final Error error) {
        mRarities = rarities;
        mError = error;
    }

    public FetchedDotaRaritiesEvent(final List<Rarity> rarities) {
        this(rarities, null);
    }

    public List<Rarity> getRarities() {
        return mRarities;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

    @Override
    public String toString() {
        return "FetchedDotaRaritiesEvent{" +
                "mRarities=" + mRarities +
                ", mError=" + mError +
                '}';
    }
}
