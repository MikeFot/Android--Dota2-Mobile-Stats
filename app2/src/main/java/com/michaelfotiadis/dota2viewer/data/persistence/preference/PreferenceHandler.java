package com.michaelfotiadis.dota2viewer.data.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Abstract class for all preference managers
 */
public abstract class PreferenceHandler {

    private final Context mContext;

    PreferenceHandler(final Context context) {
        this.mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected abstract SharedPreferences getSharedPreferences();

    protected void clear() {
        getSharedPreferences().edit().clear().apply();
    }

}
