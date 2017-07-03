package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler;

import android.content.Context;

import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchListItem;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;

public class MatchLoadingViewBinder extends BaseRecyclerViewBinder<MatchLoadingViewHolder, MatchListItem> {

    protected MatchLoadingViewBinder(final Context context) {
        super(context);
    }

    @Override
    protected void reset(final MatchLoadingViewHolder holder) {
        // NOOP
    }

    @Override
    protected void setData(final MatchLoadingViewHolder holder, final MatchListItem item) {
        // NOOP
    }
}
