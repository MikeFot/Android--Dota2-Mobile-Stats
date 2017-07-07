package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.heroes.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

class GameHeroViewHolder extends BaseRecyclerViewHolder {

    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.text_original)
    TextView mTextId;
    @BindView(R.id.text_id)
    TextView mTextOriginal;
    @BindView(R.id.image)
    ImageView mImageView;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_hero;

    GameHeroViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
