package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.recycler.GameRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.viewmodel.SteamLibraryPayload;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.viewmodel.SteamLibraryViewModel;
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
import com.michaelfotiadis.dota2viewer.utils.steam.SteamUrlUtils;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import org.greenrobot.eventbus.EventBus;

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
    ImageLoader mImageLoader;
    @Inject
    UserPreferences mUserPreferences;

    private RecyclerManager<Game> mRecyclerManager;
    private SteamLibraryViewModel mViewModel;

    @Override
    public void onListItemSelected(final View view, final Game game) {
        getIntentDispatcher().open(view, SteamUrlUtils.buildUrlFromSteamGame(game));
    }

    @Override
    public void submitQuery(final String query) {
        if (mViewModel != null) {
            mViewModel.setQuery(query);
        }
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(this).get(SteamLibraryViewModel.class);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerManager(view);

        // trying out ViewModel with EventBus
        getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void onResume() {
                EventBus.getDefault().register(mViewModel);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void onPause() {
                EventBus.getDefault().unregister(mViewModel);
            }
        });


        mViewModel.getGames(getCurrentUserId()).observe(
                this,
                new Observer<SteamLibraryPayload>() {
                    @Override
                    public void onChanged(@Nullable final SteamLibraryPayload steamLibraryPayload) {
                        AppLog.d("Received steam library payload " + steamLibraryPayload);
                        if (TextUtils.isNotEmpty(getCurrentUserId())) {
                            setResult(steamLibraryPayload);
                        } else {
                            showNoId();
                        }
                    }
                });

        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                if (mViewModel != null) {
                    if (TextUtils.isNotEmpty(getCurrentUserId())) {
                        loadData();
                    } else {
                        showNoId();
                    }

                }
            }
        });
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

        if (TextUtils.isEmpty(getCurrentUserId())) {
            showNoId();
        }
        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {

        if (mViewModel != null) {
            mRecyclerManager.clearError();
            mRecyclerManager.updateUiState(State.PROGRESS);
            mViewModel.loadGames(getCurrentUserId());
        }

    }


    private void setResult(final SteamLibraryPayload payload) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (payload.getError() == null) {

                    final List<Game> games = new ArrayList<>(payload.getGames());
                    //noinspection AnonymousInnerClassMayBeStatic
                    Collections.sort(games, new Comparator<Game>() {
                        @Override
                        public int compare(final Game o1, final Game o2) {
                            return o2.getPlaytimeForever() - (o1.getPlaytimeForever());
                        }
                    });

                    mRecyclerManager.setItems(games);
                } else {
                    final UiDataLoadError uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), payload.getError());

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
                        setRecyclerError(payload.getError());
                    }
                }
                mRecyclerManager.updateUiState();
            }
        });

    }


    public static BaseFragment newInstance() {
        return new SteamLibraryFragment();
    }


}
