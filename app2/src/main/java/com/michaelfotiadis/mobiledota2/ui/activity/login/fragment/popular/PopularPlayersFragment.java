package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.login.LoginNavigationCommand;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.recycler.PopularPlayersRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.Searchable;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.State;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class PopularPlayersFragment extends BaseRecyclerFragment<PopularPlayer> implements OnItemSelectedListener<PopularPlayer>, Searchable {

    private final List<PopularPlayer> mData = new ArrayList<>();

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<PopularPlayer> mRecyclerManager;
    @Inject
    JobScheduler mJobScheduler;

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Popular Players")
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
    protected RecyclerManager<PopularPlayer> getRecyclerManager() {
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

        mRecyclerManager = new RecyclerManager.Builder<>(new PopularPlayersRecyclerAdapter(getContext(), this))
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setEmptyMessage("Nothing to see here")
                .build();

        mRecyclerManager.updateUiState();

    }

    @Override
    protected void loadData() {

        mData.clear();

        final String[] array = getResources().getStringArray(R.array.popular_players);

        final String delimiter = ",";
        for (final String item : array) {

            if (TextUtils.isNotEmpty(item) && item.contains(delimiter)) {
                final String[] parts = item.split(delimiter);
                mData.add(new PopularPlayer(parts[0], parts[1]));
            }

        }

        mRecyclerManager.setItems(mData);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedPlayersEvent event) {

        if (event.getError() != null) {
            setRecyclerError(event.getError());
        } else if (event.getPlayers() == null || event.getPlayers().isEmpty()) {
            setRecyclerError(new Error(ErrorKind.NO_CONTENT_RETURNED));
        } else {
            AppLog.d(String.format(Locale.UK, "Showing result for %d players", event.getPlayers().size()));
            if (getActivity() instanceof LoginNavigationCommand) {
                ((LoginNavigationCommand) getActivity()).showPlayers(event.getPlayers());
            }
        }

    }

    @Override
    public void onListItemSelected(final View view, final PopularPlayer item) {
        Answers.getInstance().logCustom(new CustomEvent("Selected Popular Player").putCustomAttribute("Name", item.getName()));
        mRecyclerManager.updateUiState(State.PROGRESS);
        mJobScheduler.startFetchPlayersJob(item.getId(), false);
    }

    @Override
    public void submitQuery(final String query) {

        if (TextUtils.isNotEmpty(query)) {
            Answers.getInstance().logSearch(
                    new SearchEvent().putQuery(query).putCustomAttribute("Screen", "Popular Players"));
            final List<PopularPlayer> filtered = new ArrayList<>();
            for (final PopularPlayer player : mData) {

                if (player.getName().toLowerCase().contains(query)) {
                    filtered.add(player);
                }

            }
            mRecyclerManager.setItems(filtered);

        } else {
            mRecyclerManager.setItems(mData);
        }

    }

    public static Fragment newInstance() {
        return new PopularPlayersFragment();
    }
}
