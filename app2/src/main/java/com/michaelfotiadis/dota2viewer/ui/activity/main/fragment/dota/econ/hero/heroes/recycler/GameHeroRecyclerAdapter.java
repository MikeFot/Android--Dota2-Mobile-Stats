package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.heroes.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

public class GameHeroRecyclerAdapter extends BaseRecyclerViewAdapter<Hero, GameHeroViewHolder> {

    private final GameHeroViewBinder mBinder;


    public GameHeroRecyclerAdapter(final Context context,
                                   final ImageLoader imageLoader,
                                   final OnItemSelectedListener<Hero> listener) {
        super(context);
        this.mBinder = new GameHeroViewBinder(context, imageLoader, listener);
    }

    @Override
    protected boolean isItemValid(final Hero item) {
        return item != null && item.getId() != null;
    }

    @Override
    public GameHeroViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(GameHeroViewHolder.getLayoutId(), parent, false);
        return new GameHeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameHeroViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
