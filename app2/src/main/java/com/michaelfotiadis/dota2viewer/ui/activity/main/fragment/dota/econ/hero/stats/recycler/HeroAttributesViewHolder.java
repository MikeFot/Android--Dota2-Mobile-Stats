package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

class HeroAttributesViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_stats;

    @BindView(R.id.image_hero)
    ImageView mImageHero;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.image_pip_str)
    ImageView mImagePipStr;
    @BindView(R.id.attr_strength)
    TextView mAttrStrength;
    @BindView(R.id.image_pip_agi)
    ImageView mImagePipAgi;
    @BindView(R.id.attr_agility)
    TextView mAttrAgility;
    @BindView(R.id.image_pip_int)
    ImageView mImagePipInt;
    @BindView(R.id.attr_intelligence)
    TextView mAttrIntelligence;
    @BindView(R.id.damage_title)
    TextView mDamageTitle;
    @BindView(R.id.damage_content)
    TextView mDamageContent;
    @BindView(R.id.layout_container)
    LinearLayout mLayoutContainer;
    @BindView(R.id.card_view)
    CardView mCardView;

    HeroAttributesViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
