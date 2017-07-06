package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.model.GameDateStats;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.CalendarCard;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.CardGridItem;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.OnCellItemClick;
import com.michaelfotiadis.dota2viewer.ui.view.calendar.OnItemRender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Adapter for custom calendar
 * Created by Michael on 24/06/2015.
 */
public class DotaCalendarRecyclerAdapter extends BaseRecyclerViewAdapter<Calendar, DotaCalendarRecyclerAdapter.CalendarViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<GameDateStats> mStats;
    private final List<CalendarCard> mCalendarCardList;
    private final OnItemSelectedListener<GameDateStats> mOnClickListener;
    private OnItemRender mRenderer;

    public DotaCalendarRecyclerAdapter(final Context context,
                                       final OnItemSelectedListener<GameDateStats> listener) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mStats = new ArrayList<>();
        mOnClickListener = listener;
        mCalendarCardList = new ArrayList<>();
    }

    private OnItemRender getRenderer() {
        if (mRenderer == null) {
            mRenderer = new RendererUtils().render(
                    mStats,
                    ContextCompat.getColor(mContext, R.color.md_green_300),
                    ContextCompat.getColor(mContext, R.color.md_red_300),
                    ContextCompat.getColor(mContext, R.color.md_orange_300)
            );
        }
        return mRenderer;
    }

    public void setStats(final List<GameDateStats> statsList) {

        mStats.clear();
        mStats.addAll(statsList);

        for (final GameDateStats stat : statsList) {
            final Calendar date = Calendar.getInstance(Locale.getDefault());
            date.set(Calendar.YEAR, stat.getYear());
            date.set(Calendar.DAY_OF_YEAR, stat.getDayOfYear());
            boolean doesDateExist = false;
            for (final Calendar calendar : getItems()) {
                if (calendar.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
                    doesDateExist = true;
                }
            }
            if (!doesDateExist) {
                super.addItem(date);
            }
        }

        Collections.sort(getItems());
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return getItems() == null ? 0 : getItems().size();
    }

    @Override
    protected boolean isItemValid(final Calendar item) {
        return item != null;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        // create a new view
        final View rootView = mInflater.inflate(R.layout.list_item_calendar_card, parent, false);
        return new CalendarViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final CalendarViewHolder holder, final int position) {

        holder.calendarCard.setOnItemRender(getRenderer());
        holder.calendarCard.setDateDisplay(getItem(position));

        holder.calendarCard.setOnCellItemClick(new OnCellItemClick() {
            @SuppressWarnings("WrongConstant")
            @Override
            public void onCellClick(final View v, final CardGridItem item) {

                for (final GameDateStats stat : mStats) {
                    if (stat.getDayOfYear() == item.getDate().get(Calendar.DAY_OF_YEAR)) {
                        mOnClickListener.onListItemSelected(v, stat);
                        break;
                    }
                }
            }
        });
        mCalendarCardList.add(holder.getAdapterPosition(), holder.calendarCard);
        holder.calendarCard.notifyChanges();

    }

    @Override
    public long getItemId(final int pos) {
        return pos;
    }

    public CalendarCard getCalendarItem(final int position) {
        return mCalendarCardList.get(position);
    }

    protected static class CalendarViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.calendar_card)
        CalendarCard calendarCard;


        CalendarViewHolder(final View view) {
            super(view);
        }
    }
}
