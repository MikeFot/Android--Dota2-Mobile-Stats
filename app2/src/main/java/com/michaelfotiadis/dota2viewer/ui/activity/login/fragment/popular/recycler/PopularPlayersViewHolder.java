package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.popular.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

class PopularPlayersViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_popular_player;
    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.text_id64)
    TextView mTextId64;
    @BindView(R.id.text_id3)
    TextView mTextId3;

    PopularPlayersViewHolder(final View view) {
        super(view);
    }

    @LayoutRes
    static int getLayoutId() {
        return LAYOUT_ID;
    }

}
