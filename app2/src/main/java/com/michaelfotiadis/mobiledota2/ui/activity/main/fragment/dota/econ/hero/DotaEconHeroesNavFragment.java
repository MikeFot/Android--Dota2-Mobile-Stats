package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.heroes.DotaHeroesFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.stats.DotaHeroAttributesFragment;

import butterknife.BindView;

public class DotaEconHeroesNavFragment extends BaseBottomNavFragment {

    @BindView(R.id.navigation)
    protected BottomNavigationView mNavigationView;

    @Override
    protected BottomNavigationView getNavigationView() {
        return mNavigationView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav_dota_econ_heroes;
    }

    @Override
    protected int getFirstMenuId() {
        return R.id.navigation_heroes;
    }

    @Override
    @Nullable
    protected Fragment getFragmentForId(final int id) {

        final Fragment fragment;

        switch (id) {
            case R.id.navigation_heroes:
                fragment = DotaHeroesFragment.newInstance();
                break;
            case R.id.navigation_stats:
                fragment = DotaHeroAttributesFragment.newInstance();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    public static Fragment newInstance() {
        return new DotaEconHeroesNavFragment();
    }


}
