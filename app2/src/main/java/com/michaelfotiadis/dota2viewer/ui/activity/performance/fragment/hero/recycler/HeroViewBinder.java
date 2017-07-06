package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero.recycler;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.steam.provider.image.Size;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HeroViewBinder extends BaseRecyclerViewBinder<HeroViewHolder, HeroStatistics> {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");

    private final ImageLoader mImageLoader;

    protected HeroViewBinder(final Context context, final ImageLoader imageLoader) {
        super(context);
        mImageLoader = imageLoader;
    }

    @Override
    protected void reset(final HeroViewHolder holder) {

    }

    @Override
    protected void setData(final HeroViewHolder holder, final HeroStatistics hero) {

        // set the name
        holder.textViewHeroName.setText(hero.getLocalisedName());
        // load the imageViewHeroAvatar using Picasso

        mImageLoader.loadHero(holder.imageHero, hero.getHeroName(), Size.LARGE_HORIZONTAL);

        @DrawableRes
        final int drawableRes;
        @ColorRes
        final int tint;
        switch (hero.getPrimaryStat()) {
            case 0:
                drawableRes = R.drawable.ic_list_pip_str;
                tint = R.color.hero_card_str;
                break;
            case 1:
                drawableRes = R.drawable.ic_list_pip_agi;
                tint = R.color.hero_card_agi;
                break;
            case 2:
                drawableRes = R.drawable.ic_list_pip_int;
                tint = R.color.hero_card_int;
                break;
            default:
                drawableRes = R.drawable.ic_default;
                tint = R.color.primary_text;
        }
        holder.imageAttribute.setImageResource(drawableRes);
        holder.textViewHeroName.setTextColor(getColor(tint));

        holder.textViewHeroGamesWon.setText(String.valueOf(hero.getTimesWon()));
        holder.textViewHeroGamesLost.setText(String.valueOf(hero.getTimesPlayed() - hero.getTimesWon()));
        holder.textViewHeroWinRate.setText(String.valueOf(NumberFormat.getPercentInstance().format(hero.getAverageWins())));
        holder.textViewHeroKills.setText(String.valueOf(DECIMAL_FORMAT.format(hero.getAverageKills())));
        holder.textViewHeroDeaths.setText(String.valueOf(DECIMAL_FORMAT.format(hero.getAverageDeaths())));
        holder.textViewHeroAssists.setText(String.valueOf(DECIMAL_FORMAT.format(hero.getAverageAssists())));
        holder.textViewHeroGpM.setText(String.valueOf((int) (hero.getAverageGpM())));
        holder.textViewHeroXpM.setText(String.valueOf((int) (hero.getAverageXpM())));
        holder.textViewHeroLastHits.setText(String.valueOf((int) (hero.getAverageLastHits())));
        holder.textViewHeroDenies.setText(String.valueOf((int) (hero.getAverageDenies())));
    }
}
