package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

public class GameItemsViewBinder extends BaseRecyclerViewBinder<GameItemsViewHolder, GameItem> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<GameItem> mListener;

    protected GameItemsViewBinder(final Context context,
                                  final ImageLoader imageLoader,
                                  final OnItemSelectedListener<GameItem> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final GameItemsViewHolder holder) {
        holder.mTextView.setText("");

        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final GameItemsViewHolder holder, final GameItem item) {

        holder.mTextView.setText(item.getLocalizedName());

        mImageLoader.loadItem(holder.mImageView, item.getName());

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });
    }
}
