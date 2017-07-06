package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.recycler;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.model.GameDateStats;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.CardGridItem;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.CheckableLayout;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.OnItemRender;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Michael on 24/06/2015.
 */
public class RendererUtils {


    public OnItemRender render(final List<GameDateStats> mStats,
                               final int mColorPositive,
                               final int mColorNegative,
                               final int mColorZero) {
        return new OnItemRender() {
            @SuppressWarnings("WrongConstant")
            @Override
            public void onRender(final CheckableLayout v, final CardGridItem item) {
                ((TextView) v.getChildAt(0)).setText(String.format(Locale.US, item.getDayOfMonth().toString()));

                if (item.getDate() != null) {
                    int color = Color.WHITE;
                    if (mStats != null && item.isEnabled()) {
                        for (final GameDateStats gameDay : mStats) {
                            if (item.getDate().get(Calendar.DAY_OF_YEAR) == gameDay.getDayOfYear() &&
                                    item.getDate().get(Calendar.YEAR) == gameDay.getYear()) {
                                if (gameDay.getSum() > 0) {
                                    color = mColorPositive;
                                } else if (gameDay.getSum() < 0) {
                                    color = mColorNegative;
                                } else if (gameDay.getSum() == 0) {
                                    color = mColorZero;
                                } else {
                                    color = Color.WHITE;
                                }
                                break;
                            }
                        }
                        v.getBackground().setColorFilter(color, PorterDuff.Mode.SRC);
                    }
                    v.invalidate();
                }
            }
        };
    }
}
