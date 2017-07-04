package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile;

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
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.BaseUserRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.dota2viewer.ui.core.dialog.alert.AlertDialogFactory;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class SteamProfileFragment extends BaseUserRecyclerFragment {

    @Inject
    JobScheduler mJobScheduler;
    @Inject
    AppDatabase mAppDatabase;
    private MenuItem mDeleteItem;

    @Override
    public void onUserSelected(final View v, final PlayerSummary playerSummary) {
        getIntentDispatcher().open(v, Uri.parse(playerSummary.getProfileUrl()));
    }

    @Override
    protected void loadData() {
        mRecyclerManager.clearError();
        mRecyclerManager.updateUiState(State.PROGRESS);
        if (TextUtils.isNotEmpty(getCurrentUserId())) {
            mJobScheduler.startFetchPlayersJob(getCurrentUserId(), true);
        } else {
            if (mDeleteItem != null) {
                mDeleteItem.setVisible(false);
            }
            final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    getIntentDispatcher().openLoginActivity(v);
                }
            });

            mRecyclerManager.setError(getString(R.string.error_no_user), listenerWrapper);
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
                mJobScheduler.startDeleteProfileJob(idToDelete);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(final UserChangedEvent event) {
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedPlayersEvent event) {

        if (event.getError() == null) {
            mRecyclerManager.clearItems();
            for (final PlayerSummary summary : event.getPlayers()) {
                mRecyclerManager.addItem(new PlayerWrapper(summary));
            }
            if (mDeleteItem != null) {
                mDeleteItem.setVisible(true);
            }
        } else {
            setRecyclerError(event.getError());
        }
    }

    public static BaseFragment newInstance() {
        return new SteamProfileFragment();
    }


}
