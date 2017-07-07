package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.rarities.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

public class RaritiyRecyclerAdapter extends BaseRecyclerViewAdapter<Rarity, RarityViewHolder> {

    private final RarityViewBinder mBinder;


    public RaritiyRecyclerAdapter(final Context context,
                                  final ImageLoader imageLoader,
                                  final OnItemSelectedListener<Rarity> listener) {
        super(context);
        this.mBinder = new RarityViewBinder(context, imageLoader, listener);
    }

    @Override
    protected boolean isItemValid(final Rarity item) {
        return item != null && item.getId() != null;
    }

    @Override
    public RarityViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(RarityViewHolder.getLayoutId(), parent, false);
        return new RarityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RarityViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
