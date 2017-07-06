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
    @BindView(R.id.range_title)
    TextView mRangeTitle;
    @BindView(R.id.range_content)
    TextView mRangeContent;
    @BindView(R.id.damage_layout)
    LinearLayout mDamageLayout;
    @BindView(R.id.range_layout)
    LinearLayout mRangeLayout;
    @BindView(R.id.point_title)
    TextView mPointTitle;
    @BindView(R.id.point_content)
    TextView mPointContent;
    @BindView(R.id.point_layout)
    LinearLayout mPointLayout;
    @BindView(R.id.backswing_title)
    TextView mBackswingTitle;
    @BindView(R.id.backswing_content)
    TextView mBackswingContent;
    @BindView(R.id.backswing_layout)
    LinearLayout mBackswingLayout;
    @BindView(R.id.vision_title)
    TextView mVisionTitle;
    @BindView(R.id.vision_content)
    TextView mVisionContent;
    @BindView(R.id.vision_layout)
    LinearLayout mVisionLayout;
    @BindView(R.id.starting_attr_title)
    TextView mStartingAttrTitle;
    @BindView(R.id.starting_attr_content)
    TextView mStartingAttrContent;
    @BindView(R.id.starting_attr_layout)
    LinearLayout mStartingAttrLayout;
    @BindView(R.id.armor_title)
    TextView mArmorTitle;
    @BindView(R.id.armor_content)
    TextView mArmorContent;
    @BindView(R.id.armor_layout)
    LinearLayout mArmorLayout;
    @BindView(R.id.speed_title)
    TextView mSpeedTitle;
    @BindView(R.id.speed_content)
    TextView mSpeedContent;
    @BindView(R.id.speed_layout)
    LinearLayout mSpeedLayout;
    @BindView(R.id.turn_title)
    TextView mTurnTitle;
    @BindView(R.id.turn_content)
    TextView mTurnContent;
    @BindView(R.id.turn_layout)
    LinearLayout mTurnLayout;
    @BindView(R.id.regen_title)
    TextView mRegenTitle;
    @BindView(R.id.regen_content)
    TextView mRegenContent;
    @BindView(R.id.regen_layout)
    LinearLayout mRegenLayout;

    HeroAttributesViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
