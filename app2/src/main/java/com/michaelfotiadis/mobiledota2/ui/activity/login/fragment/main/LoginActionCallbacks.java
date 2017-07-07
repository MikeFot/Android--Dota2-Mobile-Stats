package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.main;

import android.support.annotation.NonNull;
import android.view.View;

interface LoginActionCallbacks {

    void login(@NonNull String username);

    void showLoginHelp(View view);

    void showSteamLogin(View view);

    void onPopularSelected(View view);
}
