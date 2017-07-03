package com.michaelfotiadis.dota2viewer.event.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.event.Event;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

import java.util.List;

public class FetchedDotaItemsEvent implements Event {

    private final List<GameItem> mItems;
    private final Error mError;

    public FetchedDotaItemsEvent(final List<GameItem> items, final Error error) {
        mItems = items;
        mError = error;
    }

    public FetchedDotaItemsEvent(final List<GameItem> items) {
        this(items, null);
    }

    public List<GameItem> getItems() {
        return mItems;
    }

    @Nullable
    public Error getError() {
        return mError;
    }

    @Override
    public String toString() {
        return "FetchedDota2ItemsEvent{" +
                "mItems=" + mItems +
                ", mError=" + mError +
                '}';
    }
}
