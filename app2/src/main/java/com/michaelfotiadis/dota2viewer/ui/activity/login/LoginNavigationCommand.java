package com.michaelfotiadis.dota2viewer.ui.activity.login;

import android.view.View;

import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.List;

public interface LoginNavigationCommand {

    void showPlayers(List<PlayerSummary> playerSummaries);

    void showSteamLogin(View view);

    void showHelp(View view);

    void onNavigateToPopular(View view);
}
