package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

public class LeagueListingViewBinder extends BaseRecyclerViewBinder<LeagueListingViewHolder, League> {

    private final OnItemSelectedListener<League> mListener;

    protected LeagueListingViewBinder(final Context context, final OnItemSelectedListener<League> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void reset(final LeagueListingViewHolder holder) {

    }

    @Override
    protected void setData(final LeagueListingViewHolder holder, final League item) {

        holder.mTitle.setText(item.getName());
        holder.mLeagueId.setText(String.valueOf(item.getLeagueid()));
        holder.mSummary.setText(item.getDescription());

        holder.mLeaguerUrl.setText(item.getTournamentUrl());

        holder.mLeaguerUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });


    }
}
