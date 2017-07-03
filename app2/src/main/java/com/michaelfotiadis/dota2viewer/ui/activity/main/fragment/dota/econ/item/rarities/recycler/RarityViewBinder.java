package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.rarities.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

public class RarityViewBinder extends BaseRecyclerViewBinder<RarityViewHolder, Rarity> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<Rarity> mListener;

    protected RarityViewBinder(final Context context,
                               final ImageLoader imageLoader,
                               final OnItemSelectedListener<Rarity> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final RarityViewHolder holder) {
        holder.mTextView.setText("");

        holder.getRoot().setOnClickListener(null);

    }

    @Override
    protected void setData(final RarityViewHolder holder, final Rarity item) {
        holder.mTextView.setText(item.getName());

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

    }
}
