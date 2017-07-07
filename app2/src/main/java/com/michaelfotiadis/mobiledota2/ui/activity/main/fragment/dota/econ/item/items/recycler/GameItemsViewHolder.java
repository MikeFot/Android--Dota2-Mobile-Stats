package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.items.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class GameItemsViewHolder extends BaseRecyclerViewHolder {

    @BindView(R.id.text_name)
    TextView mTextView;
    @BindView(R.id.image)
    ImageView mImageView;

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_game_item;
    @BindView(R.id.text_cost)
    TextView mTextCost;
    @BindView(R.id.text_side_shop)
    TextView mTextSideShop;
    @BindView(R.id.text_secret_shop)
    TextView mTextSecretShop;
    @BindView(R.id.layout_container)
    LinearLayout mLayoutContainer;
    @BindView(R.id.card_view)
    CardView mCardView;

    protected GameItemsViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
