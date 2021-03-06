package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.persistence.db.DbCallback;
import com.michaelfotiadis.mobiledota2.data.persistence.db.accessor.DbAccessor;
import com.michaelfotiadis.mobiledota2.data.persistence.model.Players;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.event.dota.match.FetchedDotaMatchOverviewsEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.recycler.PlayerRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler.MatchesItemAnimator;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.toast.AppToast;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;
import com.michaelfotiadis.steam.utils.SteamIdUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerPickerFragment extends BaseRecyclerFragment<PlayerWrapper> implements OnUserSelectedListener {

    private static final String EXTRA = PlayerPickerFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<PlayerWrapper> mRecyclerManager;
    protected PlayerRecyclerAdapter mAdapter;
    @Inject
    DbAccessor mDbAccessor;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Login Player Picker")
                .putContentType("Screen"));
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
        initRecyclerManager(view);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.title_select_account));
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
        final Players players = (Players) getArguments().get(EXTRA);
        if (players != null) {
            for (final PlayerSummary summary : players.getPlayers()) {
                mRecyclerManager.addItem(new PlayerWrapper(summary));
                // see if dota is allowed for account
                final String steamId3 = String.valueOf(SteamIdUtils.steamId64toSteamId3(Long.parseLong(summary.getSteamId())));
                mJobScheduler.startFetchDotaMatchOverviewsJob(steamId3, null, 1, false, false);
            }
        } else {
            mRecyclerManager.setError("Something has gone wrong");
        }
    }

    @Override
    public void onUserSelected(final View v, final PlayerSummary playerSummary) {

        AppLog.d("User selected with ID= " + playerSummary.getSteamId());

        mDbAccessor.getPlayerAccessor().insert(playerSummary, new DbCallback() {
            @Override
            public void onSuccess() {
                new UserPreferences(getContext()).writeCurrentUserId(playerSummary.getSteamId());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Answers.getInstance().logCustom(new CustomEvent("Login - Success"));

                        AppLog.d("User written with ID " + getCurrentUserId());
                        AppToast.show(getContext(), String.format("User selected: %s", playerSummary.getPersonaName()));
                        getIntentDispatcher().openMainActivity(v);
                    }
                });
            }

            @Override
            public void onFailure() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Answers.getInstance().logCustom(new CustomEvent("Login - Failure"));
                        AppToast.show(getContext(), "Something has gone wrong. Please try again.");
                        getActivity().finish();
                    }
                });
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaMatchOverviewsEvent event) {
        final String steamId64 = String.valueOf(SteamIdUtils.steamId3toSteam64(Long.parseLong(event.getId3())));
        if (event.getError() == null && event.getMatchOverviews() != null && !event.getMatchOverviews().isEmpty()) {
            mAdapter.setIsDotaAvailable(steamId64, true);
        } else if (ErrorKind.INTERNAL_SERVER_ERROR.equals(event.getError().getKind())) {
            mAdapter.setIsDotaAvailable(steamId64, false);
        }
    }

    public static BaseFragment newInstance(final List<PlayerSummary> playerSummaries) {

        final BaseFragment fragment = new PlayerPickerFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA, new Players(playerSummaries));
        fragment.setArguments(bundle);
        return fragment;

    }
}
