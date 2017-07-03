package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedLibraryEvent;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.recycler.GameRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SteamLibraryFragment extends BaseRecyclerFragment<Game> implements OnItemSelectedListener<Game>, Searchable {


    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    ImageLoader mImageLoader;
    private RecyclerManager<Game> mRecyclerManager;
    private List<Game> mData = new ArrayList<>();

    @Override
    public void onListItemSelected(final View view, final Game item) {
        // TODO implement
    }

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            mRecyclerManager.setItems(mData);
        } else {
            mRecyclerManager.setItems(new ArrayList<>(DotaGeneralUtils.getFilteredLibrary(mData, query)));
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

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerManager = new RecyclerManager.Builder<>(new GameRecyclerAdapter(getActivity(), mImageLoader, this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {
        mRecyclerManager.clearError();
        mRecyclerManager.updateUiState(State.PROGRESS);
        if (TextUtils.isNotEmpty(getCurrentUserId())) {
            mJobScheduler.startFetchLibraryJob(getCurrentUserId());
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

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLog.d("onViewCreated");
        getEventLifecycleListener().enable();
        initRecyclerManager(view);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(final UserChangedEvent event) {
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedLibraryEvent event) {

        if (event.getError() == null) {

            final List<Game> games = new ArrayList<>(event.getGames());
            //noinspection AnonymousInnerClassMayBeStatic
            Collections.sort(games, new Comparator<Game>() {
                @Override
                public int compare(final Game o1, final Game o2) {
                    return o2.getPlaytimeForever() - (o1.getPlaytimeForever());
                }
            });

            mData = Collections.unmodifiableList(games);
            mRecyclerManager.setItems(games);
        } else {
            final UiDataLoadError uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), event.getError());

            if (uiDataLoadError.getKind() == UiDataLoadError.ErrorKind.NO_DATA) {
                mRecyclerManager.setError(
                        getString(R.string.error_reason_no_games),
                        new QuoteOnClickListenerWrapper(R.string.label_try_again, new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                loadData();
                            }
                        }));
            } else {
                setRecyclerError(event.getError());
            }
        }
        mRecyclerManager.updateUiState();
    }

    public static BaseFragment newInstance() {
        return new SteamLibraryFragment();
    }


}
