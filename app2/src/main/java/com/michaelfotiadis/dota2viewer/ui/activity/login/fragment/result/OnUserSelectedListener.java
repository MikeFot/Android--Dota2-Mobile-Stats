package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result;

import android.view.View;

import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

public interface OnUserSelectedListener {

    void onUserSelected(final View v, final PlayerSummary playerSummary);

}
