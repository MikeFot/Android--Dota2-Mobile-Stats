package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.viewmodel;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import java.util.List;

public class SteamLibraryPayload {

    private final Error error;
    private final List<Game> games;

    public SteamLibraryPayload(final List<Game> games, final Error error) {
        this.error = error;
        this.games = games;
    }

    public Error getError() {
        return error;
    }

    public List<Game> getGames() {
        return games;
    }

}