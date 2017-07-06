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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.OnUserSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerWrapper;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.recycler.PlayerRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler.MatchesItemAnimator;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.viewmodel.SteamProfilePayload;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.viewmodel.SteamProfileViewModel;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.dialog.alert.AlertDialogFactory;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;

public class SteamProfileFragment extends BaseRecyclerFragment<PlayerWrapper> implements OnUserSelectedListener {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<PlayerWrapper> mRecyclerManager;
    protected PlayerRecyclerAdapter mAdapter;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    UserPreferences mUserPreferences;

    private SteamProfileViewModel mViewModel;
    private MenuItem mDeleteItem;

    @Override
    public void onUserSelected(final View v, final PlayerSummary playerSummary) {
        getIntentDispatcher().open(v, Uri.parse(playerSummary.getProfileUrl()));
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected RecyclerManager<PlayerWrapper> getRecyclerManager() {
        return mRecyclerManager;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PlayerRecyclerAdapter(getActivity(), mImageLoader, this);
        mRecyclerManager = new RecyclerManager.Builder<>(mAdapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setAnimator(new MatchesItemAnimator())
                .setEmptyMessage("Nothing to see here")
                .build();
        mRecyclerManager.updateUiState();
    }

    @Override
    protected void loadData() {
        if (mViewModel != null && TextUtils.isNotEmpty(getCurrentUserId())) {
            AppLog.d("Loading data for ID: " + getCurrentUserId());
            mRecyclerManager.clearError();
            mRecyclerManager.updateUiState(State.PROGRESS);
            mViewModel.loadProfile(getCurrentUserId());
        } else {
            showNoId();
        }
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(this).get(SteamProfileViewModel.class);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerManager(view);
        if (mRecyclerManager == null) {
            throw new NullPointerException("Null recycler");
        }

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
                        AppLog.d("Current user id is " + getCurrentUserId());
                        setResult(steamLibraryPayload);
                    }
                });

        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                AppLog.d("Current user id is " + userId);
                loadData();
            }
        });


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
            case R.id.action_add:
                getIntentDispatcher().openLoginActivity(item.getActionView());
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

        if (TextUtils.isEmpty(getCurrentUserId())) {
            showNoId();
            return;
        }

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

    protected void showNoId() {
        final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getIntentDispatcher().openLoginActivity(v);
            }
        });

        mRecyclerManager.setError(getString(R.string.error_no_user), listenerWrapper);
        mRecyclerManager.updateUiState();
    }

    public static BaseFragment newInstance() {
        return new SteamProfileFragment();
    }

}
