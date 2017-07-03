package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.recycler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.OnUserSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;

public class PlayerRecyclerAdapter extends BaseRecyclerViewAdapter<PlayerWrapper, PlayerViewHolder> {

    private final PlayerViewBinder mBinder;


    public PlayerRecyclerAdapter(final Context context,
                                 final ImageLoader imageLoader,
                                 final OnUserSelectedListener onUserSelectedListener) {
        super(context);
        this.mBinder = new PlayerViewBinder(context, imageLoader, onUserSelectedListener);
    }

    public void setIsDotaAvailable(final String steamId64, final boolean isDotaAvailable) {

        int position = 0;
        for (final PlayerWrapper wrapper : getItems()) {
            if (wrapper.getPlayerSummary().getSteamId().equals(steamId64)) {
                wrapper.setDotaAvailable(isDotaAvailable);
                notifyItemChanged(position);
                position++;
            }
        }

    }

    @Override
    protected boolean isItemValid(final PlayerWrapper item) {
        return item != null && !TextUtils.isEmpty(item.getPlayerSummary().getSteamId());
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(PlayerViewHolder.getLayoutId(), parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
