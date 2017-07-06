package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.rarities.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class RarityViewHolder extends BaseRecyclerViewHolder {

    @BindView(R.id.text_placeholder)
    TextView mTextView;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_rarity;

    protected RarityViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
