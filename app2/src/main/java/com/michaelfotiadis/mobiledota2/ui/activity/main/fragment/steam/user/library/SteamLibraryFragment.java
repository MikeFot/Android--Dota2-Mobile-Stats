package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.event.listener.EventLifecycleListener;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.recycler.GameRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.viewmodel.SteamLibraryPayload;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.viewmodel.SteamLibraryViewModel;
import com.michaelfotiadis.mobiledota2.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.mobiledota2.utils.steam.SteamUrlUtils;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SteamLibraryFragment extends BaseRecyclerFragment<Game> implements OnItemSelectedListener<Game>, Searchable {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<Game> mRecyclerManager;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    UserPreferences mUserPreferences;

    private SteamLibraryViewModel mViewModel;
    private EventLifecycleListener<SteamLibraryViewModel> mModelEventListener;

    @Override
    public void onListItemSelected(final View view, final Game game) {
        getIntentDispatcher().open(view, SteamUrlUtils.buildUrlFromSteamGame(game));
    }

    @Override
    public void submitQuery(final String query) {
        if (mViewModel != null) {
            Answers.getInstance().logSearch(
                    new SearchEvent().putQuery(query).putCustomAttribute("Screen", "Steam Library"));
            mViewModel.setQuery(query);
        }
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Steam Library")
                .putContentType("Screen"));

        mViewModel = ViewModelProviders.of(this).get(SteamLibraryViewModel.class);
        mModelEventListener = new EventLifecycleListener<>(mViewModel, getLifecycle());
        mModelEventListener.enable();

    }

    @Override
    protected RecyclerManager<Game> getRecyclerManager() {
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

        if (mViewModel != null && TextUtils.isNotEmpty(getCurrentUserId())) {
            mRecyclerManager.clearError();
            mRecyclerManager.updateUiState(State.PROGRESS);
            mViewModel.loadGames(getCurrentUserId());
        } else {
            showNoId();
        }

    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerManager(view);

        mViewModel.getGames(getCurrentUserId()).observe(
                this,
                new Observer<SteamLibraryPayload>() {
                    @Override
                    public void onChanged(@Nullable final SteamLibraryPayload steamLibraryPayload) {
                        AppLog.d("Received steam library payload " + steamLibraryPayload);
                        setResult(steamLibraryPayload);
                    }
                });

        mUserPreferences.getMutableLivePreference().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                if (mViewModel != null) {
                    loadData();
                }
            }
        });


    }

    private void setResult(final SteamLibraryPayload payload) {

        if (TextUtils.isEmpty(getCurrentUserId())) {
            showNoId();
            return;
        }

        mRecyclerManager.clearItems();
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

    protected void showNoId() {
        final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getIntentDispatcher().openLoginActivity(v);
            }
        });

        mRecyclerManager.setError(getString(R.string.error_no_user), listenerWrapper);
        mRecyclerManager.updateUiState();
    }

    public static BaseFragment newInstance() {
        return new SteamLibraryFragment();
    }

}
