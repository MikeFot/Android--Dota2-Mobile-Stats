package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user;


import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.SteamLibraryFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.SteamProfileFragment;

import butterknife.BindView;

public class SteamUserNavFragment extends BaseBottomNavFragment {


    @BindView(R.id.navigation)
    protected BottomNavigationView mNavigationView;

    @Override
    protected BottomNavigationView getNavigationView() {
        return mNavigationView;
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

    public static Fragment newInstance() {
        return new SteamUserNavFragment();
    }

}
