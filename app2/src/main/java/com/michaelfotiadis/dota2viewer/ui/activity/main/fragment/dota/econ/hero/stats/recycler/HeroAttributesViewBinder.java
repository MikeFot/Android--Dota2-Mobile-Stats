package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.provider.image.Size;

class HeroAttributesViewBinder extends BaseRecyclerViewBinder<HeroAttributesViewHolder, HeroPatchAttributes> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<HeroPatchAttributes> mListener;

    HeroAttributesViewBinder(final Context context,
                             final ImageLoader imageLoader,
                             final OnItemSelectedListener<HeroPatchAttributes> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final HeroAttributesViewHolder holder) {
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final HeroAttributesViewHolder holder, final HeroPatchAttributes item) {

        mImageLoader.loadHero(holder.mImageHero, item.getName(), Size.FULL_VERTICAL);

        holder.mName.setText(item.getHero());

        final int attrColor;
        switch (item.getAttribute()) {
            case 0:
                attrColor = R.color.hero_card_str;
                break;
            case 1:
                attrColor = R.color.hero_card_agi;
                break;
            default:
                attrColor = R.color.hero_card_int;
        }
        holder.mName.setTextColor(getColor(attrColor));


        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });


    }
}
