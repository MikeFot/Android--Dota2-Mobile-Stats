package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.michaelfotiadis.dota2viewer.BuildConfig;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.event.dota.econ.FetchedDotaItemsEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.recycler.GameItemsRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.toast.AppToast;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class DotaItemsFragment extends BaseRecyclerFragment<GameItem> implements OnItemSelectedListener<GameItem> {

    @Inject
    ImageLoader mImageLoader;
    @Inject
    JobScheduler mJobScheduler;


    @Override
    public void onListItemSelected(final View view, final GameItem item) {
        // TODO if needed
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

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerManager.updateUiState();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaItemsEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {
            AppLog.d("Loaded " + event.getItems().size() + " items");

            final List<GameItem> items = new ArrayList<>(event.getItems());
            Collections.sort(items, new Comparator<GameItem>() {
                @Override
                public int compare(final GameItem o1, final GameItem o2) {
                    return o1.getLocalizedName().compareTo(o2.getLocalizedName());
                }
            });

            mRecyclerManager.setItems(items);
        } else {
            setRecyclerError(event.getError());
        }

    }

    @Override
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerManager = new RecyclerManager.Builder<>(new GameItemsRecyclerAdapter(getActivity(), mImageLoader, this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();
        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {
        mJobScheduler.startFetchDotaItemsJob();
    }

    public static BaseFragment newInstance() {
        return new DotaItemsFragment();
    }

}
