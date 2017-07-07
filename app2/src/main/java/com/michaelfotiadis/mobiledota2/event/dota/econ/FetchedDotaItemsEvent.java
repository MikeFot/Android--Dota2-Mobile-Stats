package com.michaelfotiadis.mobiledota2.event.dota.econ;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.event.Event;
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
