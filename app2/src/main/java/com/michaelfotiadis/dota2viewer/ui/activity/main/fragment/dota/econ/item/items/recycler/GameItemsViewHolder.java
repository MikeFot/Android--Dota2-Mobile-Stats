package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class GameItemsViewHolder extends BaseRecyclerViewHolder {

    @BindView(R.id.text_name)
    TextView mTextView;
    @BindView(R.id.image)
    ImageView mImageView;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_item;

    protected GameItemsViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
