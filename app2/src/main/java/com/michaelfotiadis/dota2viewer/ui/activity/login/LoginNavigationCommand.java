package com.michaelfotiadis.dota2viewer.ui.activity.login;

import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.List;

public interface LoginNavigationCommand {

    void showPlayers(List<PlayerSummary> playerSummaries);

}
