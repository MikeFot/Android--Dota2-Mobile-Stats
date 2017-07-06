package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.BaseUserRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerWrapper;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.viewmodel.SteamProfilePayload;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.viewmodel.SteamProfileViewModel;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.dota2viewer.ui.core.dialog.alert.AlertDialogFactory;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class SteamProfileFragment extends BaseUserRecyclerFragment {

    @Inject
    UserPreferences mUserPreferences;

    private SteamProfileViewModel mViewModel;
    private MenuItem mDeleteItem;

    @Override
    public void onUserSelected(final View v, final PlayerSummary playerSummary) {
        getIntentDispatcher().open(v, Uri.parse(playerSummary.getProfileUrl()));
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerManager(view);

        getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void onResume() {
                EventBus.getDefault().register(mViewModel);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void onPause() {
                EventBus.getDefault().unregister(mViewModel);
            }
        });


        mViewModel.getProfile(getCurrentUserId()).observe(
                this,
                new Observer<SteamProfilePayload>() {
                    @Override
                    public void onChanged(@Nullable final SteamProfilePayload steamLibraryPayload) {
                        AppLog.d("Received steam library payload " + steamLibraryPayload);
                        if (TextUtils.isNotEmpty(getCurrentUserId())) {
                            setResult(steamLibraryPayload);
                        } else {
                            showNoId();
                        }
                    }
                });

        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                if (TextUtils.isNotEmpty(getCurrentUserId())) {
                    loadData();
                } else {
                    showNoId();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (mViewModel != null) {
            AppLog.d("Loading data for ID: " + getCurrentUserId());
            mRecyclerManager.clearError();
            mRecyclerManager.updateUiState(State.PROGRESS);
            mViewModel.loadProfile(getCurrentUserId());
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(this).get(SteamProfileViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mDeleteItem = menu.findItem(R.id.action_delete);
        mDeleteItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteUser(getCurrentUserId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteUser(@Nullable final String idToDelete) {

        final DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                mViewModel.deleteProfile(idToDelete);
                dialog.dismiss();
            }
        };
        final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        };

        if (TextUtils.isNotEmpty(idToDelete)) {
            new AlertDialogFactory(getActivity()).show(
                    getString(R.string.dialog_delete_title),
                    getString(R.string.dialog_delete_user_body, idToDelete),
                    getString(R.string.message_ok), okListener,
                    getString(R.string.message_cancel), cancelListener);

        } else {
            getNotificationController().showInfo(getString(R.string.toast_no_users_to_delete));
        }

    }

    private void setResult(final SteamProfilePayload steamProfilePayload) {

        if (steamProfilePayload.getError() == null) {
            mRecyclerManager.clearItems();
            for (final PlayerSummary summary : steamProfilePayload.getPlayers()) {
                mRecyclerManager.addItem(new PlayerWrapper(summary));
            }
            if (mDeleteItem != null) {
                mDeleteItem.setVisible(true);
            }
        } else {
            setRecyclerError(steamProfilePayload.getError());
        }

    }

    public static BaseFragment newInstance() {
        return new SteamProfileFragment();
    }


}
