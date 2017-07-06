package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import java.text.DecimalFormat;

public class GameViewBinder extends BaseRecyclerViewBinder<GameViewHolder, Game> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<Game> mListener;

    protected GameViewBinder(final Context context, final ImageLoader imageLoader, final OnItemSelectedListener<Game> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final GameViewHolder holder) {

        holder.title.setText("");
        holder.timePlayed.setText("");
        holder.totalTimePlayed.setText("");
        holder.gameIcon.setImageDrawable(null);
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final GameViewHolder holder, final Game game) {
        holder.title.setText(game.getName());


        holder.totalTimePlayed.setText(
                String.format(
                        getString(R.string.label_hours_played),
                        getTotalTimePlayedAsString(game.getPlaytimeForever())));
        holder.timePlayed.setText(
                String.format(
                        getString(R.string.label_last_2_weeks),
                        getTotalTimePlayedAsString(game.getPlaytimeTwoWeeks())));

        mImageLoader.loadSteamGame(holder.gameIcon, String.valueOf(game.getAppId()), game.getImgLogoUrl());

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, game);
            }
        });

    }

    @NonNull
    private static String getTotalTimePlayedAsString(final Integer minutesPlayed) {
        if (minutesPlayed == null) {
            return "0" + " hrs";
        }
        return new DecimalFormat("#0.0").format(minutesPlayed / 60f) + " hrs";
    }
}
