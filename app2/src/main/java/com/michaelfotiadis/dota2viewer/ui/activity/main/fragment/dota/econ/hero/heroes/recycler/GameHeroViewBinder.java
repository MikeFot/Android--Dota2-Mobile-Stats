package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.heroes.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.provider.image.Size;

class GameHeroViewBinder extends BaseRecyclerViewBinder<GameHeroViewHolder, Hero> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<Hero> mListener;

    GameHeroViewBinder(final Context context,
                       final ImageLoader imageLoader,
                       final OnItemSelectedListener<Hero> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final GameHeroViewHolder holder) {
        holder.mTextName.setText("");
    }

    @Override
    protected void setData(final GameHeroViewHolder holder, final Hero item) {

        holder.mTextName.setText(item.getLocalizedName());

        mImageLoader.loadHero(holder.mImageView, item.getName(), Size.FULL_HORIZONTAL);

        holder.mTextId.setText(getContext().getString(R.string.hero_id, item.getId()));
        holder.mTextOriginal.setText(
                getContext().getString(
                        R.string.hero_original, DotaGeneralUtils.getHeroNameFromDataName(item.getName())));

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

    }


}
