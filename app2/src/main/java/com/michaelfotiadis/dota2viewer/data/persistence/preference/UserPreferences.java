package com.michaelfotiadis.dota2viewer.data.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.michaelfotiadis.dota2viewer.utils.AppLog;

public class UserPreferences extends PreferenceHandler {

    private static final String SHARED_PREFS_FILE = "user.preferences";
    private static final String KEY_USER_ID = "key.user.id";
    private SharedPreferences.OnSharedPreferenceChangeListener mListener;

    public UserPreferences(final Context context) {
        super(context);
    }

    public String getCurrentUserId() {
        return getSharedPreferences().getString(KEY_USER_ID, null);
    }

    public void writeCurrentUserId(final String id) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_ID, id);
        editor.apply();
        AppLog.d("User id updated to " + id);
    }

    public void registerOnChangedListener(@NonNull final OnUserChangedListener listener) {

        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
                if (KEY_USER_ID.equals(key)) {
                    listener.onNewUser(getCurrentUserId());
                }
            }
        };

        getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);

    }

    public void unRegisterOnChangedListener() {
        if (mListener != null) {
            getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        }
    }

    @Override
    protected SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public interface OnUserChangedListener {

        void onNewUser(String username);

    }
}
