package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;

import java.util.Comparator;

public class HeroStatsRecyclerAdapter extends BaseRecyclerViewAdapter<HeroStatistics, HeroViewHolder> {

    private final HeroViewBinder mBinder;

    public HeroStatsRecyclerAdapter(final Context context,
                                    final ImageLoader imageLoader) {
        super(context);
        mBinder = new HeroViewBinder(context, imageLoader);
    }

    @Override
    public HeroViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new HeroViewHolder(LayoutInflater.from(getContext()).inflate(HeroViewHolder.getLayoutId(), parent, false));
    }


    @Override
    public void onBindViewHolder(final HeroViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }

    @Override
    protected boolean isItemValid(final HeroStatistics item) {
        return item != null;
    }

    private static class HeroComparator implements Comparator<HeroStatistics> {
        public int compare(final HeroStatistics c1, final HeroStatistics c2) {

            final float diff = c2.getAverageWins() - c1.getAverageWins();

            final float epsilon = 0.001f;

            if (diff >= epsilon) {
                return 1;
            } else if (Math.abs(diff) < epsilon) {
                return c2.getTimesPlayed() - c1.getTimesPlayed();
            } else {
                return -1;
            }

        }
    }


}