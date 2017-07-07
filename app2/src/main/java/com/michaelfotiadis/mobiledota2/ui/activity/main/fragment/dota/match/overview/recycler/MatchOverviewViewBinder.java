package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model.MatchItem;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model.MatchOverviewItem;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.mobiledota2.utils.dota.DotaTimeUtils;

public class MatchOverviewViewBinder extends BaseRecyclerViewBinder<MatchOverviewViewHolder, MatchOverviewItem> {

    private final OnItemSelectedListener<MatchItem> mListener;

    protected MatchOverviewViewBinder(final Context context,
                                      final OnItemSelectedListener<MatchItem> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void reset(final MatchOverviewViewHolder holder) {
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final MatchOverviewViewHolder holder, final MatchOverviewItem item) {

        holder.mTextMatchId.setText("Match ID: " + String.valueOf(item.getItemId()));


        final String timeAgo = DotaTimeUtils.getTimeAgoForMatch(
                getContext().getResources(),
                item.getMatchOverview().getStartTime(),
                null);
        holder.mTextContentHoursAgo.setText(timeAgo);

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mListener != null) {
                    mListener.onListItemSelected(v, item);
                }
            }
        });
    }
}
