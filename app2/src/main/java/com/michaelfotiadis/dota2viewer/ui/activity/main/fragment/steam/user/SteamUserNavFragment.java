package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.SteamLibraryFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.SteamProfileFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class SteamUserNavFragment extends BaseBottomNavFragment {

    @Inject
    UserPreferences mUserPreferences;

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

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                refreshCurrentPage();
            }
        });

    }

    public static Fragment newInstance() {
        return new SteamUserNavFragment();
    }

}
