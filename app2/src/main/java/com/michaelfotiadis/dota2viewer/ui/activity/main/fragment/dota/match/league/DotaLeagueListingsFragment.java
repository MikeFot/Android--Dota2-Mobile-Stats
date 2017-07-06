package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.event.dota.league.FetchedLeagueListingsEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.recycler.LeagueListingRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.recycler.LeaguesRecyclerContentUpdater;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaLeagueListingsFragment extends BaseRecyclerFragment<League> implements OnItemSelectedListener<League>, Searchable {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<League> mRecyclerManager;
    @Inject
    JobScheduler mJobScheduler;
    private LeaguesRecyclerContentUpdater mContentUpdater;
    private List<League> mData = new ArrayList<>();

    @Override
    public void onListItemSelected(final View view, final League item) {
        if (TextUtils.isNotEmpty(item.getTournamentUrl())) {
            getIntentDispatcher().open(view, Uri.parse(item.getTournamentUrl()));
        } else {
            getNotificationController().showAlert("Link not found for League " + item.getName());
        }
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
        initRecyclerManager(view);
        loadData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedLeagueListingsEvent event) {
        AppLog.d("FetchedLeagueListingsEvent received");
        if (event.getError() == null) {
            AppLog.d("Received " + event.getLeagues().size() + " leagues");
            mData = event.getLeagues();
            mContentUpdater.setItems(event.getLeagues());
        } else {
            setRecyclerError(event.getError());
        }
    }

    @Override
    protected RecyclerManager<League> getRecyclerManager() {
        return mRecyclerManager;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void initRecyclerManager(final View view) {

        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(false);

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerManager = new RecyclerManager.Builder<>(new LeagueListingRecyclerAdapter(getActivity(), this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mContentUpdater = new LeaguesRecyclerContentUpdater(mRecyclerManager, mRecyclerView, layoutManager);

        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {
        AppLog.d("Loading league listings");
        mJobScheduler.startFetchDotaLeagueListingsJob();
    }


    @Override
    public void submitQuery(final String query) {
        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            mRecyclerManager.setItems(DotaGeneralUtils.getFilteredLeagues(mData, query));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaLeagueListingsFragment();
    }

}
