package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;
import com.michaelfotiadis.dota2viewer.ui.view.ThreeBarView;

import butterknife.BindColor;
import butterknife.BindView;

class MatchDetailsViewHolder extends BaseRecyclerViewHolder {


    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_match_details;
    @BindView(R.id.image_hero)
    ImageView mImageHero;
    @BindView(R.id.text_content_victory)
    TextView mTextContentVictory;
    @BindView(R.id.text_date)
    TextView mTextContentHoursAgo;
    @BindView(R.id.text_content_match_duration)
    TextView mTextContentMatchDuration;
    @BindView(R.id.text_lobby)
    TextView mTextContentLobbyType;
    @BindView(R.id.text_content_last_hits)
    TextView mTextLastHits;
    @BindView(R.id.text_content_game_mode)
    TextView mTextContentGameMode;
    @BindView(R.id.text_content_leaver)
    TextView mTextLeaver;
    @BindView(R.id.text_content_kda)
    TextView mTextContentKda;
    @BindView(R.id.bar_three_color)
    ThreeBarView mBarThreeColor;
    @BindView(R.id.card_view)
    CardView mCardView;

    @BindColor(R.color.md_green_500)
    int mColorKills;
    @BindColor(R.color.md_red_500)
    int mColorDeaths;
    @BindColor(R.color.md_light_blue_500)
    int mColorAssists;

    MatchDetailsViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
