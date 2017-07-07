package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result;


import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

public class PlayerWrapper {

    private final PlayerSummary mPlayerSummary;
    private Boolean mIsDotaAvailable;

    public PlayerWrapper(final PlayerSummary playerSummary) {
        mPlayerSummary = playerSummary;
    }

    public Boolean getDotaAvailable() {
        return mIsDotaAvailable;
    }

    public void setDotaAvailable(final Boolean dotaAvailable) {
        mIsDotaAvailable = dotaAvailable;
    }


    public PlayerSummary getPlayerSummary() {
        return mPlayerSummary;
    }
}
