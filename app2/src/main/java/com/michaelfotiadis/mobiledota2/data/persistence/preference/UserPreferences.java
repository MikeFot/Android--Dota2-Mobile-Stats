package com.michaelfotiadis.mobiledota2.data.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;

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

        if (TextUtils.isEmpty(getCurrentUserId()) && TextUtils.isEmpty(id)) {
            return;
        } else if (TextUtils.isNotEmpty(getCurrentUserId()) && TextUtils.isNotEmpty(id) && getCurrentUserId().equals(id)) {
            return;
        }

        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_ID, id);
        editor.apply();
        AppLog.d("User id updated to " + id);
    }

    public LiveSharedPreference<String> getMutableLivePreference() {
        return new LiveSharedPreference<>(KEY_USER_ID, getSharedPreferences());
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
