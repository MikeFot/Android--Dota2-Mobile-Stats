package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.profile.viewmodel;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.List;

public class SteamProfilePayload {

    private final List<PlayerSummary> mPlayers;
    private final Error mError;

    public SteamProfilePayload(final List<PlayerSummary> players, final Error error) {
        mPlayers = players;
        mError = error;
    }

    public List<PlayerSummary> getPlayers() {
        return mPlayers;
    }

    public Error getError() {
        return mError;
    }
}
