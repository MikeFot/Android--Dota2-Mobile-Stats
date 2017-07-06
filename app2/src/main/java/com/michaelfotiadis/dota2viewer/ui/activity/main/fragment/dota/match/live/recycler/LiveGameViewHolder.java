package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.live.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class LiveGameViewHolder extends BaseRecyclerViewHolder {


    @BindView(R.id.league_title)
    TextView mLeagueTitle;
    @BindView(R.id.radiant_score)
    TextView mRadiantScore;
    @BindView(R.id.dire_score)
    TextView mDireScore;
    @BindView(R.id.match_id)
    TextView mMatchId;
    @BindView(R.id.duration)
    TextView mDuration;
    @BindView(R.id.spectators)
    TextView mSpectators;
    @BindView(R.id.layout_duration)
    ViewGroup mLayoutDuration;
    @BindView(R.id.radiant_faction)
    ViewGroup mRadiant;
    @BindView(R.id.dire_faction)
    ViewGroup mDire;
    final LiveFactionViewHolder mRadiantViewHolder;
    final LiveFactionViewHolder mDireViewHolder;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_live_game;

    protected LiveGameViewHolder(final View view) {
        super(view);
        mRadiantViewHolder = new LiveFactionViewHolder(mRadiant);
        mDireViewHolder = new LiveFactionViewHolder(mDire);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
