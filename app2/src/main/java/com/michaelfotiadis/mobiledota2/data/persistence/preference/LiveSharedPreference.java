package com.michaelfotiadis.mobiledota2.data.persistence.preference;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;

public class LiveSharedPreference<T> extends MutableLiveData<T> {

    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.OnSharedPreferenceChangeListener mListener;


    public LiveSharedPreference(final String preferenceKey,
                                final SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {

                if (key.equals(preferenceKey)) {
                    final T value = (T) sharedPreferences.getAll().get(key);
                    setValue(value);
                }

            }
        };


    }

    @Override
    protected void onActive() {
        super.onActive();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(null);
    }
}

