package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.SteamLibraryFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.SteamProfileFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SteamUserNavFragment extends BaseBottomNavFragment {

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav_steam_user;
    }

    @Override
    protected int getFirstMenuId() {
        return R.id.navigation_profile;
    }

    @Override
    protected Fragment getFragmentForId(final int id) {

        final Fragment fragment;

        switch (id) {
            case R.id.navigation_profile:
                fragment = SteamProfileFragment.newInstance();
                break;
            case R.id.navigation_library:
                fragment = SteamLibraryFragment.newInstance();
                break;
            default:
                fragment = null;
        }
        return fragment;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(final UserChangedEvent event) {
        refreshCurrentPage();
    }

    public static Fragment newInstance() {
        return new SteamUserNavFragment();
    }

}
