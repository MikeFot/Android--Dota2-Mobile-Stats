package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class MatchErrorViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_match_error;
    @BindView(R.id.progress)
    ImageView mProgress;
    @BindView(R.id.text_match_id)
    TextView mTextMatchId;
    @BindView(R.id.text_error)
    TextView mTextError;
    @BindView(R.id.layout_info)
    LinearLayout mLayoutInfo;
    @BindView(R.id.card_view)
    CardView mCardView;


    protected MatchErrorViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
