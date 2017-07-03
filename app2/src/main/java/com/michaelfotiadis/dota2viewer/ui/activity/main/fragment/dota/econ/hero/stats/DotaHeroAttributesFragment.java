package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.michaelfotiadis.dota2viewer.BuildConfig;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.event.dota.stats.FetchedDotaHeroPatchAttributesEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats.recycler.HeroAttributesRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.toast.AppToast;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class DotaHeroAttributesFragment extends BaseRecyclerFragment<HeroPatchAttributes>
        implements OnItemSelectedListener<HeroPatchAttributes>, Searchable {

    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;
    private List<HeroPatchAttributes> mData = new ArrayList<>();

    @Override
    public void onListItemSelected(final View view, final HeroPatchAttributes item) {
        // TODO
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
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerManager = new RecyclerManager.Builder<>(new HeroAttributesRecyclerAdapter(getActivity(), mImageLoader, this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();
    }

    @Override
    protected void loadData() {
        mJobScheduler.startFetchHeroAttributesJob(getResources());
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
        initRecyclerManager(view);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaHeroPatchAttributesEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {
            AppLog.d("Loaded " + event.getHeroes().size() + " details");

            mData = new ArrayList<>(event.getHeroes());
            Collections.sort(mData, new Comparator<HeroPatchAttributes>() {
                @Override
                public int compare(final HeroPatchAttributes o1, final HeroPatchAttributes o2) {
                    return o1.getHero().compareTo(o2.getHero());
                }
            });
            mRecyclerManager.setItems(mData);
        } else {
            setRecyclerError(event.getError());
        }
    }

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            mRecyclerManager.setItems(DotaGeneralUtils.getFilteredHeroPatchList(mData, query));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaHeroAttributesFragment();
    }

}
