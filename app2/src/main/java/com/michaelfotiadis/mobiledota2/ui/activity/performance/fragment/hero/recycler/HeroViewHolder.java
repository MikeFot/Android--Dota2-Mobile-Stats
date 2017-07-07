package com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.hero.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

class HeroViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_hero_stats;

    @BindView(R.id.label)
    TextView textViewHeroName;
    @BindView(R.id.image_hero)
    ImageView imageHero;
    @BindView(R.id.image_attribute)
    ImageView imageAttribute;
    @BindView(R.id.text_content_games_won)
    TextView textViewHeroGamesWon;
    @BindView(R.id.text_content_games_lost)
    TextView textViewHeroGamesLost;
    @BindView(R.id.text_content_win_rate)
    TextView textViewHeroWinRate;
    @BindView(R.id.text_content_kills)
    TextView textViewHeroKills;
    @BindView(R.id.text_content_assists)
    TextView textViewHeroAssists;
    @BindView(R.id.text_content_deaths)
    TextView textViewHeroDeaths;
    @BindView(R.id.text_content_gpm)
    TextView textViewHeroGpM;
    @BindView(R.id.text_content_xpm)
    TextView textViewHeroXpM;
    @BindView(R.id.text_content_last_hits)
    TextView textViewHeroLastHits;
    @BindView(R.id.text_content_denies)
    TextView textViewHeroDenies;


    HeroViewHolder(final View view) {
        super(view);
    }

    @LayoutRes
    protected static int getLayoutId() {
        return LAYOUT_ID;
    }
}