package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.dota2viewer.event.dota.econ.FetchedDotaHeroesEvent;
import com.michaelfotiadis.dota2viewer.event.dota.econ.FetchedDotaItemsEvent;
import com.michaelfotiadis.dota2viewer.event.dota.match.FetchedDotaMatchDetailsEvent;
import com.michaelfotiadis.dota2viewer.event.dota.match.FetchedDotaMatchOverviewsEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchDetailsItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchErrorItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchListItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler.MatchRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler.MatchRecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.EndlessRecyclerOnScrollListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.match.overview.MatchOverview;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DotaMatchOverviewFragment extends BaseFragment implements OnItemSelectedListener<MatchItem> {

    private static final int MAX_MATCHES = 200;
    private static final int REQUESTED_INITIAL_MATCHES = 30;
    private static final int REQUESTED_ADDITIONAL_MATCHES = 10;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;

    private MenuItem mCalendarMenu;
    private MenuItem mRefreshMenu;
    private MatchRecyclerManager mRecyclerManager;
    private long mCurrentUserId3;

    @Override
    public void onListItemSelected(final View view, final MatchItem item) {

        if (item.getClass().isAssignableFrom(MatchErrorItem.class)) {
            mRecyclerManager.setLoading(item.getItemId());
            mJobScheduler.startFetchDotaMatchDetailsJob(item.getItemId());
        } else if (item.getClass().isAssignableFrom(MatchDetailsItem.class)) {
            final MatchDetailsItem matchDetailsItem = (MatchDetailsItem) item;

            final MatchContainer matchContainer = new MatchContainer(
                    mCurrentUserId3,
                    matchDetailsItem.getMatchDetails(),
                    mRecyclerManager.getDotaHeroes(), mRecyclerManager.getDotaItems());

            getIntentDispatcher().openMatchDetailsActivity(view, matchContainer);
        }

    }

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
        getEventLifecycleListener().enable();

        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(false);

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int current_page) {
                AppLog.d("OnLoadMore: " + current_page);
                if (!mRecyclerManager.isLoadingMore()) {
                    loadMore();
                }

            }
        });

        mRecyclerManager = new MatchRecyclerManager(
                new RecyclerManager.Builder<>(new MatchRecyclerAdapter(getActivity(), mImageLoader, this))
                        .setRecycler(mRecyclerView)
                        .setStateKeeper(uiStateKeeper)
                        .setEmptyMessage("Nothing to see here"));

        mRecyclerManager.updateUiState();

        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecyclerManager.getItemCount() > 0 && mRecyclerManager.getItemCount() <= MAX_MATCHES) {
            final List<MatchListItem> items = mRecyclerManager.getItems();
            for (final MatchListItem item : items) {
                if (item.isOverviewItem() || item.isErrorItem()) {
                    AppLog.d("Will retry to fetch match details: " + item.getItemId());
                    mJobScheduler.startFetchDotaMatchDetailsJob(item.getItemId());
                }
                if (item.isLoadingItem()) {
                    loadMore();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_matches, menu);

        mRefreshMenu = menu.findItem(R.id.action_refresh);
        mCalendarMenu = menu.findItem(R.id.action_calendar);
        mCalendarMenu.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                getIntentDispatcher().openPerformanceActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void loadMore() {
        if (mRecyclerManager.getItemCount() >= REQUESTED_INITIAL_MATCHES && mRecyclerManager.getItemCount() < MAX_MATCHES) {
            final Long lastId = mRecyclerManager.getLastValidId();
            if (lastId != null) {
                // this is done on a new handler in order to show it on the next ui frame
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerManager.addLoadingItem();
                        AppLog.d("ScrollListener: Loading more for last ID " + lastId);
                        // matches + 1 because initial Sequence Number match will be included in the results
                        mJobScheduler.startFetchDotaMatchOverviewsJob(String.valueOf(mCurrentUserId3), lastId, REQUESTED_ADDITIONAL_MATCHES + 1, true, false);

                    }
                }, 100);
                final String message = getString(R.string.toast_fetching_more, REQUESTED_ADDITIONAL_MATCHES);
                getNotificationController().showInfo(message);
            } else {
                AppLog.d("ScrollListener: Last id is null");
            }
        } else if (mRecyclerManager.getItemCount() >= MAX_MATCHES) {
            final String message = getString(R.string.toast_fetched_max_matches);
            getNotificationController().showInfo(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaHeroesEvent event) {
        if (event.getError() == null) {
            mRecyclerManager.setDotaHeroes(event.getHeroes());
            checkLoaded();
        } else {
            mRecyclerManager.setError(UiDataLoadErrorFactory.createError(getContext(), event.getError()).getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaItemsEvent event) {
        if (event.getError() == null) {
            mRecyclerManager.setDotaItems(event.getItems());
            checkLoaded();
        } else {
            setError(UiDataLoadErrorFactory.createError(getContext(), event.getError()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaMatchDetailsEvent event) {
        if (event.getError() == null) {
            mRecyclerManager.add(mCurrentUserId3, event.getMatch());
            checkLoaded();
        } else {
            setError(UiDataLoadErrorFactory.createError(getContext(), event.getError()));
        }
    }

    private void setError(final UiDataLoadError uiDataLoadError) {

        if (uiDataLoadError.getKind() == UiDataLoadError.ErrorKind.NO_NETWORK) {
            showNoNetworkMessage();
        }

        mRecyclerManager.setError(
                uiDataLoadError.getMessage(),
                new QuoteOnClickListenerWrapper(R.string.label_try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loadData();
                    }
                }));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedDotaMatchOverviewsEvent event) {
        if (event.getError() == null) {
            final List<MatchListItem> items = new ArrayList<>();
            for (final MatchOverview overview : event.getMatchOverviews()) {
                items.add(new MatchListItem(overview));
            }
            mRecyclerManager.removeAllLoadingItems();
            mRecyclerManager.setItems(items);
        } else {
            setError(UiDataLoadErrorFactory.createError(getContext(), event.getError()));
        }
    }

    protected void loadData() {
        mRecyclerManager.clearError();
        mRecyclerManager.updateUiState(State.PROGRESS);
        if (getCurrentUserId3() != null) {
            mCurrentUserId3 = getCurrentUserId3();
            mJobScheduler.startFetchDotaMatchOverviewsJob(
                    String.valueOf(mCurrentUserId3), null, REQUESTED_INITIAL_MATCHES, true, true);
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

    private void checkLoaded() {
        // random null pointer that can happen when resuming
        final boolean isLoading = mRecyclerManager.isLoading();
        if (mCalendarMenu != null) {
            mCalendarMenu.setVisible(!isLoading);
        }
        if (mRefreshMenu != null) {
            if (isLoading) {
                if (mRefreshMenu.getActionView() == null) {
                    mRefreshMenu.setActionView(R.layout.refresh_progress);
                    mRefreshMenu.setVisible(true);
                }
            } else {
                if (mRefreshMenu.getActionView() != null) {
                    mRefreshMenu.setVisible(false);
                    mRefreshMenu.setActionView(null);
                    getNotificationController().showInfo(getString(R.string.toast_data_loaded, mRecyclerManager.getItemCount()));
                }
            }
        }


    }

    public static BaseFragment newInstance() {
        return new DotaMatchOverviewFragment();
    }

}
