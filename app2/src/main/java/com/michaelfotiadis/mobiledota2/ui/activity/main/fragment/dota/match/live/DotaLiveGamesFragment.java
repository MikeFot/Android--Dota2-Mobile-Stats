package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaHeroesEvent;
import com.michaelfotiadis.mobiledota2.event.dota.league.FetchedLeagueListingsEvent;
import com.michaelfotiadis.mobiledota2.event.dota.league.FetchedLiveLeagueEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live.recycler.LiveGameRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.mobiledota2.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.mobiledota2.utils.dota.SearchFilterUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaLiveGamesFragment extends BaseRecyclerFragment<LiveGame> implements OnItemSelectedListener<LiveGame>, Searchable {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<LiveGame> mRecyclerManager;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    ImageLoader mImageLoader;

    private List<League> mLeagues = new ArrayList<>();
    private List<Hero> mHeroes = new ArrayList<>();
    private List<LiveGame> mGames = new ArrayList<>();
    private LiveGameRecyclerAdapter mAdapter;

    @Override
    public void onListItemSelected(final View view, final LiveGame item) {


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
    public void onDataLoadedEvent(final FetchedLiveLeagueEvent event) {
        AppLog.d("Live Games loaded");
        if (event.getError() == null) {
            mGames = event.getLiveGames();

            Collections.sort(mGames, new Comparator<LiveGame>() {
                @Override
                public int compare(final LiveGame o1, final LiveGame o2) {

                    final long d1 = DotaGeneralUtils.getDurationSafe(o1);
                    final long d2 = DotaGeneralUtils.getDurationSafe(o2);
                    //noinspection IfMayBeConditional
                    if (d1 == d2 && o2.getMatchId() != null && o1.getMatchId() != null) {
                        return (int) (o2.getMatchId() - o1.getMatchId());
                    } else {
                        return (int) (d2 - d1);
                    }
                }
            });

            mRecyclerManager.setItems(mGames);
        } else {
            setRecyclerError(event.getError());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaHeroesEvent event) {
        AppLog.d("Heroes loaded");
        if (event.getError() == null) {
            mAdapter.setHeroes(event.getHeroes());
            mHeroes = event.getHeroes();
        } else {
            setRecyclerError(event.getError());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedLeagueListingsEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {
            mLeagues = event.getLeagues();
            mAdapter.setLeagues(event.getLeagues());
        } else {
            setRecyclerError(event.getError());
        }
    }

    @Override
    protected RecyclerManager<LiveGame> getRecyclerManager() {
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

        mAdapter = new LiveGameRecyclerAdapter(getActivity(), mImageLoader, this);

        mRecyclerManager = new RecyclerManager.Builder<>(mAdapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setAnimator(new DefaultItemAnimator())
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Hero> heroes = DotaHeroEntity.fromEntities(mAppDatabase.getDotaHeroDao().getAllSync());
                if (heroes == null || heroes.isEmpty()) {
                    mJobScheduler.startFetchDotaHeroesJob();
                } else {
                    mAdapter.setHeroes(heroes);
                }
                mJobScheduler.startFetchDotaLeagueListingsJob();
                mJobScheduler.startFetchDotaLiveGamesJob();
            }
        }).start();

    }

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mGames);
        } else {
            Answers.getInstance().logSearch(
                    new SearchEvent().putQuery(query).putCustomAttribute("Screen", "Dota Live Games"));
            mRecyclerManager.setItems(new ArrayList<>(SearchFilterUtils.getFilteredLiveGames(mGames, mLeagues, mHeroes, query)));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaLiveGamesFragment();
    }

}
