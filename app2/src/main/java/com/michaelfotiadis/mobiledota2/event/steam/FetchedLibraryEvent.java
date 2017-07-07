package com.michaelfotiadis.mobiledota2.event.steam;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import java.util.List;

public class FetchedLibraryEvent implements Event {

    private final List<Game> mGames;
    private final Error mError;

    public FetchedLibraryEvent(final List<Game> games, final Error error) {
        mGames = games;
        mError = error;
    }

    public FetchedLibraryEvent(final List<Game> games) {
        mGames = games;
        mError = null;

    }

    public List<Game> getGames() {
        return mGames;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

}
