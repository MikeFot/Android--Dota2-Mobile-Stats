package com.michaelfotiadis.dota2viewer.event.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

import java.util.List;

public class FetchedDotaHeroesEvent implements Event {

    private final List<Hero> mHeroes;
    private final Error mError;

    public FetchedDotaHeroesEvent(final List<Hero> heroes, final Error error) {
        mHeroes = heroes;
        mError = error;
    }

    public FetchedDotaHeroesEvent(final List<Hero> heroes) {
        this(heroes, null);
    }

    public List<Hero> getHeroes() {
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
