package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.items;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.michaelfotiadis.mobiledota2.BuildConfig;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaItemsEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.items.recycler.GameItemsRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.toast.AppToast;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.mobiledota2.utils.dota.SearchFilterUtils;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaItemsFragment extends BaseRecyclerFragment<GameItem> implements OnItemSelectedListener<GameItem>, Searchable {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<GameItem> mRecyclerManager;
    private final List<GameItem> mData = new ArrayList<>();
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
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Dota Items Overview")
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
        mRecyclerManager.updateUiState();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaItemsEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {
            AppLog.d("Loaded " + event.getItems().size() + " items");

            mData.clear();
            mData.addAll(event.getItems());
            Collections.sort(mData, new Comparator<GameItem>() {
                @Override
                public int compare(final GameItem o1, final GameItem o2) {
                    return o1.getLocalizedName().compareTo(o2.getLocalizedName());
                }
            });

            mRecyclerManager.setItems(mData);
        } else {
            setRecyclerError(event.getError());
        }

    }

    @Override
    protected RecyclerManager<GameItem> getRecyclerManager() {
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

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(layoutManager);
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

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            Answers.getInstance().logSearch(
                    new SearchEvent().putQuery(query).putCustomAttribute("Screen", "Dota Items"));
            mRecyclerManager.setItems(SearchFilterUtils.getFilteredGameItems(mData, query));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaItemsFragment();
    }

}
