package com.michaelfotiadis.mobiledota2.event.dota.stats;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.mobiledota2.event.Event;

import java.util.List;

public class FetchedDotaHeroPatchAttributesEvent implements Event {

    private final List<HeroPatchAttributes> mHeroes;
    private final Error mError;

    public FetchedDotaHeroPatchAttributesEvent(final List<HeroPatchAttributes> heroes, final Error error) {
        mHeroes = heroes;
        mError = error;
    }

    public FetchedDotaHeroPatchAttributesEvent(final List<HeroPatchAttributes> heroes) {
        this(heroes, null);
    }

    public List<HeroPatchAttributes> getHeroes() {
        return mHeroes;
    }

    @Nullable
    public Error getError() {
        return mError;
    }
}
