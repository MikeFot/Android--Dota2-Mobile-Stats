package com.michaelfotiadis.mobiledota2.ui.activity.details.match.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.view.holder.BaseViewHolder;

import butterknife.BindView;

public class MatchDetailsViewHolder extends BaseViewHolder {
    @BindView(R.id.text_header_victory)
    public TextView textHeaderVictory;
    @BindView(R.id.faction_radiant_details)
    public LinearLayout layoutRadiantFaction;
    @BindView(R.id.faction_dire_details)
    public LinearLayout layoutDireFaction;


    public MatchDetailsViewHolder(final View rootView) {
        super(rootView);
    }
}