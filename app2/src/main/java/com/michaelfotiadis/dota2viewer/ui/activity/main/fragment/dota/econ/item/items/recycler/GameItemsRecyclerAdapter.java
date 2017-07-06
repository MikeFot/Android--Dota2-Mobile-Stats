package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

public class GameItemsRecyclerAdapter extends BaseRecyclerViewAdapter<GameItem, GameItemsViewHolder> {

    private final GameItemsViewBinder mBinder;


    public GameItemsRecyclerAdapter(final Context context,
                                    final ImageLoader imageLoader,
                                    final OnItemSelectedListener<GameItem> listener) {
        super(context);
        this.mBinder = new GameItemsViewBinder(context, imageLoader, listener);
    }

    @Override
    protected boolean isItemValid(final GameItem item) {
        return item != null && item.getId() != null;
    }

    @Override
    public GameItemsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(GameItemsViewHolder.getLayoutId(), parent, false);
        return new GameItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameItemsViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
