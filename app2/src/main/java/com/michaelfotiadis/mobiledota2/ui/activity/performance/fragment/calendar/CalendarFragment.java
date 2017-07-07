package com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchDetailsEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.dialog.WinsDialog;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.model.GameDateStats;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.recycler.DotaCalendarRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    AppDatabase mAppDatabase;
    private RecyclerManager<Calendar> mRecyclerManager;
    private DotaCalendarRecyclerAdapter mAdapter;

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Performance - Calendar")
                .putContentType("Screen"));
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_default_recycler, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final OnItemSelectedListener<GameDateStats> listener =
                new OnItemSelectedListener<GameDateStats>() {
                    @Override
                    public void onListItemSelected(final View view, final GameDateStats stats) {
                        showDialog(stats);
                    }
                };

        mAdapter = new DotaCalendarRecyclerAdapter(getActivity(), listener);
        mRecyclerManager = new RecyclerManager.Builder<>(mAdapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();

        loadData();
    }

    private void loadData() {

        if (getCurrentUserId3() != null) {
            fetchFromDb(getCurrentUserId3());
        } else {
            final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    getIntentDispatcher().openLoginActivity(v);
                }
            });
            mRecyclerManager.setError(getString(R.string.error_no_user), listenerWrapper);
        }
    }

    protected void fetchFromDb(final Long userId3) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                final List<DotaMatchDetailsEntity> entities = mAppDatabase.getDotaMatchDetailsDao().getAllSync();

                final List<MatchDetails> matchDetailsList = new ArrayList<>();
                for (final DotaMatchDetailsEntity entity : entities) {

                    final MatchDetails details = entity.getDetails();

                    for (final PlayerDetails playerDetails : details.getPlayers()) {
                        if (playerDetails.getAccountId().equals(userId3)) {
                            matchDetailsList.add(details);
                        }
                    }

                }
                AppLog.d("Found " + matchDetailsList.size() + " details for user id " + userId3);


                final List<GameDateStats> stats = DotaGeneralUtils.getGameDateStatsFromMatchHistory(userId3, matchDetailsList);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (stats.size() == 0) {
                            getActivity().setTitle(getString(R.string.title_no_matches));
                            mRecyclerManager.setError(UiDataLoadErrorFactory.createGenericError(getContext()).getMessage());
                        } else {
                            getActivity().setTitle(getString(R.string.title_last_matches, matchDetailsList.size()));
                            mAdapter.setStats(stats);
                        }
                        mRecyclerManager.updateUiState();
                    }
                });


            }
        }).start();

    }

    private void showDialog(final GameDateStats stats) {
        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        final DialogFragment dialogFragment = WinsDialog.newInstance(stats);
        dialogFragment.show(ft, "dialog");
    }

    public static Fragment newInstance() {
        return new CalendarFragment();
    }


}
