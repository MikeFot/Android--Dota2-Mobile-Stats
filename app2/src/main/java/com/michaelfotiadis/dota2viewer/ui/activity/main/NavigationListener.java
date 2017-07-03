package com.michaelfotiadis.dota2viewer.ui.activity.main;

import android.view.View;

interface NavigationListener {

    void onSteamUserSelected();

    void onDotaMatchesSelected();

    void onDotaEconHeroesSelected();

    void onDotaEconItemsSelected();

    void onAddProfile(final View view);

    void onProfileSelected(final View view, long identifier);

}