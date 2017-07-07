package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class MatchOverviewViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_match_overview;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.text_match_id)
    TextView mTextMatchId;
    @BindView(R.id.text_content_hours_ago)
    TextView mTextContentHoursAgo;
    @BindView(R.id.card_view)
    CardView mCardView;

    protected MatchOverviewViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
