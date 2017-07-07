package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model.MatchErrorItem;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model.MatchItem;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;

public class MatchErrorViewBinder extends BaseRecyclerViewBinder<MatchErrorViewHolder, MatchErrorItem> {

    private final OnItemSelectedListener<MatchItem> mListener;

    protected MatchErrorViewBinder(final Context context,
                                   final OnItemSelectedListener<MatchItem> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void reset(final MatchErrorViewHolder holder) {
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final MatchErrorViewHolder holder, final MatchErrorItem item) {

        holder.mTextMatchId.setText("Match ID: " + String.valueOf(item.getItemId()));
        holder.mTextError.setText(item.getError().getMessage());

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
