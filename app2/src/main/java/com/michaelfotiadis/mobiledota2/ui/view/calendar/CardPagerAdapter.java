package com.michaelfotiadis.mobiledota2.ui.view.calendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.Calendar;

public class CardPagerAdapter extends PagerAdapter {

    private Context mContext;
    private OnCellItemClick defaultOnCellItemClick;

    public CardPagerAdapter(final Context ctx) {
        mContext = ctx;
    }

    @Override
    public Object instantiateItem(final View collection, final int position) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position);
        final CalendarCard card = new CalendarCard(mContext);
        card.setDateDisplay(cal);
        card.notifyChanges();
        if (card.getOnCellItemClick() == null)
            card.setOnCellItemClick(defaultOnCellItemClick);

        ((ViewPager) collection).addView(card, 0);

        return card;
    }

    @Override
    public void destroyItem(final View collection, final int position, final Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(final View arg0) {
    }

    @Override
    public void restoreState(final Parcelable arg0, final ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(final View arg0) {
    }

    @Override
    public int getCount() {
        // TODO almoast ifinite ;-)
        return Integer.MAX_VALUE;
    }

    public OnCellItemClick getDefaultOnCellItemClick() {
        return defaultOnCellItemClick;
    }

    public void setDefaultOnCellItemClick(final OnCellItemClick defaultOnCellItemClick) {
        this.defaultOnCellItemClick = defaultOnCellItemClick;
    }

}
