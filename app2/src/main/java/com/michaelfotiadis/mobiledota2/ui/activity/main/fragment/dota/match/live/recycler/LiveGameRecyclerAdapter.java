package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;

import java.util.ArrayList;
import java.util.List;

public class LiveGameRecyclerAdapter extends BaseRecyclerViewAdapter<LiveGame, LiveGameViewHolder> {

    private LiveGameViewBinder mBinder;
    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<LiveGame> mListener;
    private final List<Hero> mHeroes = new ArrayList<>();
    private final List<League> mLeagues = new ArrayList<>();

    public LiveGameRecyclerAdapter(final Context context,
                                   final ImageLoader imageLoader,
                                   final OnItemSelectedListener<LiveGame> listener) {
        super(context);
        setHasStableIds(true);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).getMatchId();
    }

    public void setLeagues(final List<League> leagues) {
        mLeagues.clear();
        mLeagues.addAll(leagues);
    }

    public void setHeroes(final List<Hero> heroes) {
        mHeroes.clear();
        mHeroes.addAll(heroes);
    }

    @Override
    protected boolean isItemValid(final LiveGame item) {
        // abort if we do not have heroes!
        return item != null && !mHeroes.isEmpty() && !mLeagues.isEmpty();
    }

    @Override
    public LiveGameViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(LiveGameViewHolder.getLayoutId(), parent, false);
        return new LiveGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LiveGameViewHolder holder, final int position) {

        if (mBinder == null) {
            if (mHeroes.isEmpty()) {
                return;
            } else {
                mBinder = new LiveGameViewBinder(getContext(), mImageLoader, mHeroes, mLeagues, mListener);
            }
        }

        mBinder.bind(holder, getItem(position));
    }

}
