package com.michaelfotiadis.mobiledota2.event.dota.stats;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;

import java.util.List;

public class FetchedDotaHeroDetailsEvent implements Event {

    private final List<HeroDetails> mHeroes;
    private final Error mError;

    public FetchedDotaHeroDetailsEvent(final List<HeroDetails> heroes, final Error error) {
        mHeroes = heroes;
        mError = error;
    }

    public FetchedDotaHeroDetailsEvent(final List<HeroDetails> heroes) {
        this(heroes, null);
    }

    public List<HeroDetails> getHeroes() {
        return mHeroes;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

    @Override
    public String toString() {
        return "FetchedDotaHeroesEvent{" +
                "mHeroes=" + mHeroes +
                ", mError=" + mError +
                '}';
    }
}
