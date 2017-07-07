package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class LiveFiveHeroViewHolder extends BaseRecyclerViewHolder {


    @LayoutRes
    private static final int LAYOUT_ID = R.layout.includable_five_hero_horizontal;
    @BindView(R.id.image_hero_1)
    ImageView mImageHero1;
    @BindView(R.id.image_hero_2)
    ImageView mImageHero2;
    @BindView(R.id.image_hero_3)
    ImageView mImageHero3;
    @BindView(R.id.image_hero_4)
    ImageView mImageHero4;
    @BindView(R.id.image_hero_5)
    ImageView mImageHero5;

    protected LiveFiveHeroViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }

}
