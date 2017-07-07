package com.michaelfotiadis.mobiledota2.ui.activity.details.match.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.view.holder.BaseViewHolder;

import butterknife.BindView;

public class MatchStatsRowViewHolder extends BaseViewHolder {
    @BindView(R.id.image_hero)
    public ImageView imageHero;
    @BindView(R.id.text_view_hero_name)
    public TextView textHeroName;
    @BindView(R.id.text_level)
    public TextView textLevel;
    @BindView(R.id.text_kills)
    public TextView textKills;
    @BindView(R.id.text_deaths)
    public TextView textDeaths;
    @BindView(R.id.text_assists)
    public TextView textAssists;
    @BindView(R.id.text_gold)
    public TextView textGold;
    @BindView(R.id.text_spent)
    public TextView textSpent;
    @BindView(R.id.text_last_hits)
    public TextView textLastHits;
    @BindView(R.id.text_denies)
    public TextView textDenies;
    @BindView(R.id.text_xpm)
    public TextView textXpm;
    @BindView(R.id.text_gpm)
    public TextView textGpm;
    @BindView(R.id.text_hd)
    public TextView textHd;
    @BindView(R.id.text_hh)
    public TextView textHh;
    @BindView(R.id.text_td)
    public TextView textTd;
    @BindView(R.id.item_view)
    public LinearLayout itemView;


    public MatchStatsRowViewHolder(final View rootView) {
        super(rootView);
    }
}