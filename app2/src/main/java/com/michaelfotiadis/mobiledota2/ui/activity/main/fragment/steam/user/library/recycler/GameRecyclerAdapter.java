package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

public class GameRecyclerAdapter extends BaseRecyclerViewAdapter<Game, GameViewHolder> {

    private final GameViewBinder mBinder;


    public GameRecyclerAdapter(final Context context,
                               final ImageLoader imageLoader,
                               final OnItemSelectedListener<Game> listener) {
        super(context);
        this.mBinder = new GameViewBinder(context, imageLoader, listener);
    }

    @Override
    protected boolean isItemValid(final Game item) {
        return item != null && item.getAppId() != null;
    }

    @Override
    public GameViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(GameViewHolder.getLayoutId(), parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
