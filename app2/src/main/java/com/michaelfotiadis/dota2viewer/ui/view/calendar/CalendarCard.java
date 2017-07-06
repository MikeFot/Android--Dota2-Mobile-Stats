package com.michaelfotiadis.dota2viewer.ui.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarCard extends RelativeLayout {
    private final List<CheckableLayout> cells = new ArrayList<CheckableLayout>();
    private int itemLayout = R.layout.includable_card_item_simple;
    private TextView cardTitle;
    private OnItemRender mOnItemRender;
    private OnItemRender mOnItemRenderDefault;
    private OnCellItemClick mOnCellItemClick;
    private Calendar dateDisplay;
    private LinearLayout cardGrid;

    public CalendarCard(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarCard(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCard(final Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        if (isInEditMode()) return;
        final View layout = LayoutInflater.from(context).inflate(R.layout.list_item_card_view, null, false);

        if (dateDisplay == null) {
            dateDisplay = Calendar.getInstance();
        }

        cardTitle = (TextView) layout.findViewById(R.id.cardTitle);
        cardGrid = (LinearLayout) layout.findViewById(R.id.cardGrid);

        cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        ((TextView) layout.findViewById(R.id.cardDay1)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay2)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay3)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay4)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay5)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay6)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ((TextView) layout.findViewById(R.id.cardDay7)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

        final LayoutInflater la = LayoutInflater.from(context);
        for (int y = 0; y < cardGrid.getChildCount(); y++) {
            final LinearLayout row = (LinearLayout) cardGrid.getChildAt(y);
            for (int x = 0; x < row.getChildCount(); x++) {
                final CheckableLayout cell = (CheckableLayout) row.getChildAt(x);
                cell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        for (final CheckableLayout c : cells)
                            c.setChecked(false);
                        ((CheckableLayout) v).setChecked(true);

                        if (getOnCellItemClick() != null)
                            getOnCellItemClick().onCellClick(v, (CardGridItem) v.getTag()); // TODO create item
                    }
                });
                cell.addView(la.inflate(itemLayout, cell, false));
                cells.add(cell);
            }
        }

        addView(layout);

        mOnItemRenderDefault = new OnItemRender() {
            @Override
            public void onRender(final CheckableLayout v, final CardGridItem item) {
                ((TextView) v.getChildAt(0)).setText(item.getDayOfMonth().toString());
                v.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.DARKEN);
                v.invalidate();
            }
        };
        updateCells();
    }

    private int getDaySpacing(final int dayOfWeek) {
        if (Calendar.SUNDAY == dayOfWeek)
            return 6;
        else
            return dayOfWeek - 2;
    }

    private int getDaySpacingEnd(final int dayOfWeek) {
        return 8 - dayOfWeek;
    }

    private void updateCells() {
        Calendar cal;
        Integer counter = 0;
        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);

        int daySpacing = getDaySpacing(cal.get(Calendar.DAY_OF_WEEK));

        // INFO : wrong calculations of first line - fixed
        if (daySpacing > 0) {
            final Calendar prevMonth = (Calendar) cal.clone();
            prevMonth.add(Calendar.MONTH, -1);
            prevMonth.set(Calendar.DAY_OF_MONTH, prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - daySpacing + 1);
            for (int i = 0; i < daySpacing; i++) {
                final CheckableLayout cell = cells.get(counter);
                cell.setTag(new CardGridItem(prevMonth.get(Calendar.DAY_OF_MONTH)).setEnabled(false));
                cell.setEnabled(false);
                (mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem) cell.getTag());
                counter++;
                prevMonth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        final int firstDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        final int lastDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        for (int i = firstDay; i < lastDay; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i - 1);
            final Calendar date = (Calendar) cal.clone();
            date.add(Calendar.DAY_OF_MONTH, 1);
            final CheckableLayout cell = cells.get(counter);
            cell.setTag(new CardGridItem(i).setEnabled(true).setDate(date));
            cell.setEnabled(true);
            cell.setVisibility(View.VISIBLE);
            (mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem) cell.getTag());
            counter++;
        }

        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        daySpacing = getDaySpacingEnd(cal.get(Calendar.DAY_OF_WEEK));

        if (daySpacing > 0) {
            for (int i = 0; i < daySpacing; i++) {
                final CheckableLayout cell = cells.get(counter);
                cell.setTag(new CardGridItem(i + 1).setEnabled(false)); // .setDate((Calendar)cal.clone())
                cell.setEnabled(false);
                cell.setVisibility(View.VISIBLE);
                mOnItemRenderDefault.onRender(cell, (CardGridItem) cell.getTag());
//                cell.setBackgroundColor(Color.WHITE);
//                (mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem) cell.getTag());
                counter++;
            }
        }

        if (counter < cells.size()) {
            for (int i = counter; i < cells.size(); i++) {
                cells.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);
        /*if (changed && cells.size() > 0) {
            final int size = (r - l) / 7;
            for (final CheckableLayout cell : cells) {
                cell.getLayoutParams().width = size;
                cell.getLayoutParams().height = size;
            }
        }*/
    }

    public int getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(final int itemLayout) {
        this.itemLayout = itemLayout;
        //mCardGridAdapter.setItemLayout(itemLayout);
    }

    public OnItemRender getOnItemRender() {
        return mOnItemRender;
    }

    public void setOnItemRender(final OnItemRender mOnItemRender) {
        this.mOnItemRender = mOnItemRender;
        //mCardGridAdapter.setOnItemRender(mOnItemRender);
    }

    public Calendar getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(final Calendar dateDisplay) {
        this.dateDisplay = dateDisplay;
        cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
    }

    public OnCellItemClick getOnCellItemClick() {
        return mOnCellItemClick;
    }

    public void setOnCellItemClick(final OnCellItemClick mOnCellItemClick) {
        this.mOnCellItemClick = mOnCellItemClick;
    }

    /**
     * call after change any input data - to refresh view
     */
    public void notifyChanges() {
        //mCardGridAdapter.init();
        updateCells();
    }

}
