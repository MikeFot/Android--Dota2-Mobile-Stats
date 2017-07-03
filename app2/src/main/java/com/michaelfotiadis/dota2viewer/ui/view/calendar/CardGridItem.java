package com.michaelfotiadis.dota2viewer.ui.view.calendar;

import java.util.Calendar;

public class CardGridItem {

    private Integer dayOfMonth;
    private Object data;
    private boolean enabled = true;
    private Calendar date;

    public CardGridItem(final Integer dom) {
        setDayOfMonth(dom);
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public CardGridItem setDayOfMonth(final Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CardGridItem setData(final Object data) {
        this.data = data;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CardGridItem setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Calendar getDate() {
        return date;
    }

    public CardGridItem setDate(final Calendar date) {
        this.date = date;
        return this;
    }

}
