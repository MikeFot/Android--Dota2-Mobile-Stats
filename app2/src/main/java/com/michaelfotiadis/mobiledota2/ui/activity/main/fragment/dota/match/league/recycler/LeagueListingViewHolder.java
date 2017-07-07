package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.league.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class LeagueListingViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_league_listing;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.summary)
    TextView mSummary;
    @BindView(R.id.league_id)
    TextView mLeagueId;
    @BindView(R.id.league_url_content)
    TextView mLeaguerUrl;
    @BindView(R.id.card_view)
    CardView mCardView;

    protected LeagueListingViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
