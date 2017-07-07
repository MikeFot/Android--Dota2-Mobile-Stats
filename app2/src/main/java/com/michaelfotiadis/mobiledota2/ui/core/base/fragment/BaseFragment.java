package com.michaelfotiadis.mobiledota2.ui.core.base.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.event.listener.EventLifecycleListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch.IntentDispatcher;
import com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch.IntentDispatcherImpl;
import com.michaelfotiadis.mobiledota2.ui.core.toast.ActivityNotificationController;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.steam.utils.SteamIdUtils;

public abstract class BaseFragment extends Fragment implements LifecycleRegistryOwner {

    private LifecycleRegistry mLifecycleRegistry;
    private EventLifecycleListener mEventLifecycleListener;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mLifecycleRegistry = new LifecycleRegistry(this);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    protected EventLifecycleListener getEventLifecycleListener() {
        if (mEventLifecycleListener == null) {
            mEventLifecycleListener = new EventLifecycleListener<>(this, getLifecycle());
        }
        return mEventLifecycleListener;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        final View view = getActivity().getCurrentFocus();
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected IntentDispatcher getIntentDispatcher() {
        return new IntentDispatcherImpl(getActivity());
    }


    @Nullable
    protected String getCurrentUserId() {
        return new UserPreferences(getActivity()).getCurrentUserId();
    }

    @Nullable
    protected Long getCurrentUserId3() {

        if (TextUtils.isEmpty(getCurrentUserId())) {
            return null;
        } else {
            if (SteamIdUtils.isSteamId64(getCurrentUserId())) {
                return SteamIdUtils.steamId64toSteamId3(Long.parseLong(getCurrentUserId()));
            } else {
                return Long.valueOf(getCurrentUserId());
            }
        }
    }

    protected void showNoNetworkMessage() {

        getNotificationController().showAlert(
                R.string.toast_no_network,
                R.string.label_no_network_action,
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        getIntentDispatcher().showSystemWirelessSettings();
                    }
                });

    }

    protected ActivityNotificationController getNotificationController() {
        return ((BaseActivity) getActivity()).getNotificationController();
    }

}
