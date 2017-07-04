package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.popular.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.popular.PopularPlayer;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;

class PopularPlayersViewBinder extends BaseRecyclerViewBinder<PopularPlayersViewHolder, PopularPlayer> {

    private final OnItemSelectedListener<PopularPlayer> mListener;

    PopularPlayersViewBinder(final Context context,
                             final OnItemSelectedListener<PopularPlayer> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void reset(final PopularPlayersViewHolder holder) {
        holder.mTextName.setText("");
        holder.mTextId.setText("");
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final PopularPlayersViewHolder holder, final PopularPlayer item) {


        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

        holder.mTextName.setText(item.getName());
        holder.mTextId.setText(item.getId());

    }


}
