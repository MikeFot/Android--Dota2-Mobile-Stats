package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.heroes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.michaelfotiadis.dota2viewer.BuildConfig;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.event.dota.econ.FetchedDotaHeroesEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.heroes.recycler.GameHeroRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.toast.AppToast;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.SearchFilterUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class DotaHeroesFragment extends BaseRecyclerFragment<Hero> implements OnItemSelectedListener<Hero>, Searchable {

    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;
    private List<Hero> mData = new ArrayList<>();

    @Override
    public void onListItemSelected(final View view, final Hero item) {
        // TODO implement as needed
        if (BuildConfig.DEBUG) {
            AppToast.show(getContext(), "Not implemented yet");
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
    public void onDataLoadedEvent(final FetchedDotaHeroesEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {

            mData = new ArrayList<>(event.getHeroes());
            Collections.sort(mData, new Comparator<Hero>() {
                @Override
                public int compare(final Hero o1, final Hero o2) {
                    return o1.getLocalizedName().compareTo(o2.getLocalizedName());
                }
            });

            mRecyclerManager.setItems(mData);
        } else {
            setRecyclerError(event.getError());
        }

    }

    @Override
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity()) + 1;
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerManager = new RecyclerManager.Builder<>(new GameHeroRecyclerAdapter(getActivity(), mImageLoader, this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();
    }

    @Override
    protected void loadData() {
        mJobScheduler.startFetchDotaHeroesJob();
    }

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            mRecyclerManager.setItems(SearchFilterUtils.getFilteredHeroList(mData, query));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaHeroesFragment();
    }

}
