package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.rarities.recycler;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

import org.apache.commons.lang3.text.WordUtils;

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
        holder.mTextName.setText("");

        holder.getRoot().setOnClickListener(null);

    }

    @Override
    protected void setData(final RarityViewHolder holder, final Rarity item) {

        final int bgColor = Color.parseColor(item.getColor());

        final double threshold = (Color.red(bgColor) * 0.299 + Color.green(bgColor) * 0.587 + Color.blue(bgColor) * 0.114);

        final int textColor = threshold > 186 ? R.color.md_black_1000 : R.color.md_white_1000;

        holder.mTextName.setText(WordUtils.capitalizeFully(item.getName()));
        holder.mTextName.setTextColor(getColor(textColor));

        showView(holder.mTextId, item.getId() != null);
        if (item.getId() != null) {
            holder.mTextId.setText(getContext().getString(R.string.rarity_id, item.getId().toString()));
            holder.mTextId.setTextColor(getColor(textColor));
        }

        holder.mCardView.setCardBackgroundColor(Color.parseColor(item.getColor()));

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

    }
}
