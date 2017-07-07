package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.rarities.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

class RarityViewHolder extends BaseRecyclerViewHolder {

    @BindView(R.id.text_name)
    TextView mTextName;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_rarity;
    @BindView(R.id.text_id)
    TextView mTextId;
    @BindView(R.id.layout_container)
    LinearLayout mLayoutContainer;
    @BindView(R.id.card_view)
    CardView mCardView;

    RarityViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
