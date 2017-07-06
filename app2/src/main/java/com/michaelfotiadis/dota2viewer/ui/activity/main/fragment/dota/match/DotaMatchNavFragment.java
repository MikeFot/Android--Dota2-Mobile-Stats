package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.DotaLeagueListingsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.live.DotaLiveGamesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.DotaMatchOverviewFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaMatchNavFragment extends BaseBottomNavFragment {

    @BindView(R.id.navigation)
    protected BottomNavigationView mNavigationView;

    @Inject
    UserPreferences mUserPreferences;
    @Inject
    JobScheduler mJobScheduler;

    @Override
    protected BottomNavigationView getNavigationView() {
        return mNavigationView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav_dota_matches;
    }

    @Override
    protected int getFirstMenuId() {
        return R.id.navigation_history;
    }

    @Override
    protected Fragment getFragmentForId(final int id) {
        final Fragment fragment;
        switch (id) {
            case R.id.navigation_history:
                fragment = DotaMatchOverviewFragment.newInstance();
                break;
            case R.id.navigation_leagues:
                fragment = DotaLeagueListingsFragment.newInstance();
                break;
            case R.id.navigation_live:
                fragment = DotaLiveGamesFragment.newInstance();
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

                if (mNavigationView.getSelectedItemId() == R.id.navigation_history) {
                    refreshCurrentPage();
                }

            }
        });

    }

    public static BaseFragment newInstance() {
        return new DotaMatchNavFragment();
    }


}
