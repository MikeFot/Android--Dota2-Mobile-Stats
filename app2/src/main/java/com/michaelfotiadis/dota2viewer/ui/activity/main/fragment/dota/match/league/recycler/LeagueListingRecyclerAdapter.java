package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

public class LeagueListingRecyclerAdapter extends BaseRecyclerViewAdapter<League, LeagueListingViewHolder> {

    private final LeagueListingViewBinder mBinder;


    public LeagueListingRecyclerAdapter(final Context context,
                                        final OnItemSelectedListener<League> listener) {
        super(context);
        this.mBinder = new LeagueListingViewBinder(context, listener);
    }

    @Override
    protected boolean isItemValid(final League item) {
        return item != null && TextUtils.isNotEmpty(item.getName()) && TextUtils.isNotEmpty(item.getTournamentUrl());
    }

    @Override
    public LeagueListingViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(LeagueListingViewHolder.getLayoutId(), parent, false);
        return new LeagueListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LeagueListingViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
