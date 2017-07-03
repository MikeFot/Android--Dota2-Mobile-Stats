package com.michaelfotiadis.dota2viewer.utils.steam;

import android.net.Uri;

import com.michaelfotiadis.steam.data.steam.player.library.Game;

/**
 *
 */
public final class SteamUrlUtils {

    private final static String APP_STEAM_STORE_BASE_URL = "http://store.steampowered.com/app/";
    private final static String STEAM_STORE_BASE_URL = "http://store.steampowered.com/";

    private SteamUrlUtils() {
    }


    public static Uri buildUrlFromSteamGame(final Game game) {
        if (game == null) {
            return Uri.parse(STEAM_STORE_BASE_URL);
        }

        final String urlBuilder = APP_STEAM_STORE_BASE_URL +
                game.getAppId() +
                "//";

        return Uri.parse(urlBuilder);
    }


}
