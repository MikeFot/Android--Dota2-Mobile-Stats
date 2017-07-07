package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.PopularPlayer;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;

public class PopularPlayersRecyclerAdapter extends BaseRecyclerViewAdapter<PopularPlayer, PopularPlayersViewHolder> {

    private final PopularPlayersViewBinder mBinder;


    public PopularPlayersRecyclerAdapter(final Context context,
                                         final OnItemSelectedListener<PopularPlayer> onSelectedListener) {
        super(context);
        this.mBinder = new PopularPlayersViewBinder(context, onSelectedListener);
    }


    @Override
    protected boolean isItemValid(final PopularPlayer item) {
        return item != null && TextUtils.isNotEmpty(item.getName()) && TextUtils.isNotEmpty(item.getId());
    }

    @Override
    public PopularPlayersViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(PopularPlayersViewHolder.getLayoutId(), parent, false);
        return new PopularPlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PopularPlayersViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
