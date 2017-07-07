package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.stats.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;

public class HeroAttributesRecyclerAdapter extends BaseRecyclerViewAdapter<HeroPatchAttributes, HeroAttributesViewHolder> {

    private final HeroAttributesViewBinder mBinder;


    public HeroAttributesRecyclerAdapter(final Context context,
                                         final ImageLoader imageLoader,
                                         final OnItemSelectedListener<HeroPatchAttributes> listener) {
        super(context);
        this.mBinder = new HeroAttributesViewBinder(context, imageLoader, listener);
    }

    @Override
    protected boolean isItemValid(final HeroPatchAttributes item) {
        return item != null && TextUtils.isNotEmpty(item.getHero());
    }

    @Override
    public HeroAttributesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(HeroAttributesViewHolder.getLayoutId(), parent, false);
        return new HeroAttributesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HeroAttributesViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

}
