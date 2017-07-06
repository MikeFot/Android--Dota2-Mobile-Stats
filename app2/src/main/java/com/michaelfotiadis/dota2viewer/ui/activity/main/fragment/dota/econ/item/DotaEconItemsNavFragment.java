package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.BaseBottomNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.DotaItemsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.rarities.DotaRaritiesFragment;

public class DotaEconItemsNavFragment extends BaseBottomNavFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav_dota_econ_items;
    }

    @Override
    protected int getFirstMenuId() {
        return R.id.navigation_items;
    }

    @Override
    @Nullable
    protected Fragment getFragmentForId(final int id) {

        final Fragment fragment;

        switch (id) {
            case R.id.navigation_items:
                fragment = DotaItemsFragment.newInstance();
                break;
            case R.id.navigation_rarities:
                fragment = DotaRaritiesFragment.newInstance();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    public static Fragment newInstance() {
        return new DotaEconItemsNavFragment();
    }


}
