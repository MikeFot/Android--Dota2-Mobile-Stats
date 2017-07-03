package com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaMatchDetailsEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.dota2viewer.event.dota.stats.FetchedDotaHeroPatchAttributesEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero.recycler.HeroStatsRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaStatsFactory;
import com.michaelfotiadis.dota2viewer.utils.dota.SearchFilterUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class HeroStatsFragment extends BaseRecyclerFragment<HeroStatistics> {


    @Inject
    AppDatabase mAppDatabase;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    JobScheduler mJobScheduler;

    private List<HeroStatistics> mData = new ArrayList<>();


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
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        final HeroStatsRecyclerAdapter adapter = new HeroStatsRecyclerAdapter(getActivity(), mImageLoader);
        mRecyclerManager = new RecyclerManager.Builder<>(adapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();

        initRecyclerManager(view);

        loadData();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchMenu = menu.findItem(R.id.action_search);

        // Initialise the search view
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity.getSupportActionBar() == null) {
            return;
        }

        final SearchView searchView = new SearchView(activity.getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(searchMenu, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(searchMenu, searchView);
        // Add a OnQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchText) {
                submitQuery(searchText);
                return false;
            }
        });

        // Add an expand listener
        MenuItemCompat.setOnActionExpandListener(searchMenu, new DefaultOnActionExpandListener());
    }

    private void submitQuery(final String query) {
        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            mRecyclerManager.setItems(SearchFilterUtils.getFilteredHeroStatistics(mData, query));
        }

    }

    @Override
    protected void loadData() {

        if (getCurrentUserId3() != null) {
            mJobScheduler.startFetchHeroAttributesJob(getResources());
        } else {
            showNoUserError();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaHeroPatchAttributesEvent event) {
        if (event.getError() == null) {
            fetchFromDb(getCurrentUserId3(), event.getHeroes());
        } else {
            final UiDataLoadError uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), event.getError());
            if (uiDataLoadError.isRecoverable()) {
                mRecyclerManager.setError(uiDataLoadError.getMessage(), new QuoteOnClickListenerWrapper(R.string.label_try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loadData();
                    }
                }));
            } else {
                mRecyclerManager.setError(uiDataLoadError.getMessage());
            }
        }
    }

    protected void fetchFromDb(final Long userId3, final List<HeroPatchAttributes> heroDetailsList) {

        if (userId3 == null) {
            showNoUserError();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                final List<MatchDetails> matches = DotaMatchDetailsEntity.fromEntities(
                        mAppDatabase.getDotaMatchDetailsDao().getAllSync());
                final List<Hero> heroes = DotaHeroEntity.fromEntities(
                        mAppDatabase.getDotaHeroDao().getAllSync());

                final List<HeroStatistics> heroStatsList = new ArrayList<>();

                final DotaStatsFactory factory = new DotaStatsFactory(userId3, matches, heroDetailsList);
                for (final Hero hero : heroes) {
                    AppLog.d("Looking for stats for hero " + hero.getLocalizedName());
                    heroStatsList.add(factory.getStatsForHero(hero));
                }

                Collections.sort(heroStatsList, new HeroStatisticsComparator());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mData = heroStatsList;
                        mRecyclerManager.setItems(heroStatsList);
                    }
                });


            }
        }).start();

    }

    public static Fragment newInstance() {
        return new HeroStatsFragment();
    }


    private static class HeroStatisticsComparator implements Comparator<HeroStatistics> {
        @Override
        public int compare(final HeroStatistics o1, final HeroStatistics o2) {

            //noinspection IfMayBeConditional
            if (o1.getTimesPlayed() == o2.getTimesPlayed()) {
                return o1.getLocalisedName().compareTo(o2.getLocalisedName());
            } else {
                return o2.getTimesPlayed() - o1.getTimesPlayed();
            }
        }
    }


}
