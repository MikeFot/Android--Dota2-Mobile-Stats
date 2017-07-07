package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class LiveFactionViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.includable_live_faction;
    @BindView(R.id.team_title)
    TextView mTeamTitle;
    @BindView(R.id.faction_title)
    TextView mFactionTitle;
    @BindView(R.id.picks)
    TextView mPicks;
    @BindView(R.id.bans)
    TextView mBans;
    @BindView(R.id.text_net_worth)
    TextView mNetWorth;
    @BindView(R.id.faction_picks)
    ViewGroup mFactionPicks;
    @BindView(R.id.faction_bans)
    ViewGroup mFactionBans;

    final LiveFiveHeroViewHolder mPicksHolder;
    final LiveFiveHeroViewHolder mBansHolder;


    protected LiveFactionViewHolder(final View view) {
        super(view);
        mPicksHolder = new LiveFiveHeroViewHolder(mFactionPicks);
        mBansHolder = new LiveFiveHeroViewHolder(mFactionBans);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }

}
