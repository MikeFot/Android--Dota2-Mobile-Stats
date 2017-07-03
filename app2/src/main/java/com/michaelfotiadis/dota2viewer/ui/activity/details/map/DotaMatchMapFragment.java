package com.michaelfotiadis.dota2viewer.ui.activity.details.map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.details.BaseDetailsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.activity.details.map.view.MapViewBinder;
import com.michaelfotiadis.dota2viewer.ui.activity.details.map.view.MapViewHolder;

public class DotaMatchMapFragment extends BaseDetailsFragment {

    public static Fragment newInstance(final MatchContainer match) {
        final DotaMatchMapFragment fragment = new DotaMatchMapFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA, match);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MapViewHolder viewHolder = new MapViewHolder(view);
        final MapViewBinder binder = new MapViewBinder(getContext());
        if (mMatch != null) {
            binder.bind(viewHolder, mMatch.getMatchDetails());
        }
    }

}
