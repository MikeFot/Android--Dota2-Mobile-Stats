package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.stats;

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
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.mobiledota2.event.dota.stats.FetchedDotaHeroPatchAttributesEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.stats.recycler.HeroAttributesRecyclerAdapter;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DotaHeroAttributesFragment extends BaseRecyclerFragment<HeroPatchAttributes>
        implements OnItemSelectedListener<HeroPatchAttributes>, Searchable {


    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<HeroPatchAttributes> mRecyclerManager;

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
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Dota Hero Attributes")
                .putContentType("Screen"));
    }

    @Override
    protected RecyclerManager<HeroPatchAttributes> getRecyclerManager() {
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
            Answers.getInstance().logSearch(
                    new SearchEvent().putQuery(query).putCustomAttribute("Screen", "Dota Hero Attributes"));
            mRecyclerManager.setItems(SearchFilterUtils.getFilteredHeroPatchList(mData, query));
        }

    }

    public static BaseFragment newInstance() {
        return new DotaHeroAttributesFragment();
    }

}
