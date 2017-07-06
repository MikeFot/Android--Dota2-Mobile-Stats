package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.DotaLeagueListingsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.live.DotaLiveGamesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.DotaMatchOverviewFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DotaMatchNavFragment extends BaseBottomNavFragment {

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(final UserChangedEvent event) {
        refreshCurrentPage();
    }

    public static BaseFragment newInstance() {
        return new DotaMatchNavFragment();
    }


}
