package com.michaelfotiadis.mobiledota2.ui.activity.main;

import android.view.View;

interface NavigationListener {

    void onSteamUserSelected();

    void onDotaMatchesSelected();

    void onDotaEconHeroesSelected();

    void onDotaEconItemsSelected();

    void onRateSelected();

    void onAboutSelected();

    void onAddProfile(final View view);

    void onProfileSelected(final View view, long identifier);

    void onDeleteProfile(long identifier);
}