package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.rarities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.michaelfotiadis.mobiledota2.BuildConfig;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.event.dota.econ.FetchedDotaRaritiesEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.rarities.recycler.RaritiyRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.toast.AppToast;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.rarity.Rarity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaRaritiesFragment extends BaseRecyclerFragment<Rarity> implements OnItemSelectedListener<Rarity> {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<Rarity> mRecyclerManager;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void onListItemSelected(final View view, final Rarity item) {
        // TODO
        if (BuildConfig.DEBUG) {
            AppToast.show(getContext(), "Not implemented yet");
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    protected RecyclerManager<Rarity> getRecyclerManager() {
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

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity()) + 1;
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerManager = new RecyclerManager.Builder<>(new RaritiyRecyclerAdapter(getActivity(), mImageLoader, this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();
        mRecyclerManager.updateUiState();
    }

    @Override
    protected void loadData() {
        mJobScheduler.startFetchDotaRaritiesJob();
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Dota Rarities")
                .putContentType("Screen"));
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEventLifecycleListener().enable();
        initRecyclerManager(view);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaRaritiesEvent event) {
        AppLog.d("Data loaded");
        if (event.getError() == null) {
            AppLog.d("Loaded " + event.getRarities().size() + " rarities");

            final List<Rarity> data = new ArrayList<>(event.getRarities());
            Collections.sort(data, new Comparator<Rarity>() {
                @Override
                public int compare(final Rarity o1, final Rarity o2) {
                    return o2.getOrder() - o1.getOrder();
                }
            });

            mRecyclerManager.setItems(event.getRarities());
        } else {
            setRecyclerError(event.getError());
        }
    }

    public static BaseFragment newInstance() {
        return new DotaRaritiesFragment();
    }

}
