package com.michaelfotiadis.dota2viewer.ui.core.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero.HeroStatsFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseRecyclerFragment<D> extends BaseFragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<D> mRecyclerManager;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof Searchable) {
            setHasOptionsMenu(true);
        }
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

    protected void setRecyclerError(final Error error) {

        final UiDataLoadError uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), error);
        if (uiDataLoadError.isRecoverable()) {

            if (uiDataLoadError.getKind() == UiDataLoadError.ErrorKind.NO_NETWORK) {
                showNoNetworkMessage();
            }
            mRecyclerManager.setError(uiDataLoadError.getMessage(), new QuoteOnClickListenerWrapper(R.string.label_try_again, new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    loadData();
                }
            }));

        } else {
            mRecyclerManager.setError(uiDataLoadError.getMessage());
        }

    }

    protected void showNoUserError() {
        final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getIntentDispatcher().openLoginActivity(v);
            }
        });
        mRecyclerManager.setError(getString(R.string.error_no_user), listenerWrapper);
    }

    protected abstract void initRecyclerManager(View view);

    protected abstract void loadData();

    private boolean isSearchable() {
        return this instanceof Searchable;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

        if (isSearchable()) {
            inflater.inflate(R.menu.menu_search, menu);

            final MenuItem searchMenu = menu.findItem(R.id.action_search);

            // Initialise the search view
            final AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() == null) {
                return;
            }

            final SearchView searchView = new SearchView(activity.getSupportActionBar().getThemedContext());
            MenuItemCompat.setShowAsAction(searchMenu, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setActionView(searchMenu, searchView);
            // Add a OnQueryTextListener
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
                    mRecyclerView.getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                        @Override
                        public void onAnimationsFinished() {
                            ((Searchable) BaseRecyclerFragment.this).submitQuery(query);
                        }
                    });

                    return true;
                }

                @Override
                public boolean onQueryTextChange(final String searchText) {
                    mRecyclerView.getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                        @Override
                        public void onAnimationsFinished() {
                            ((Searchable) BaseRecyclerFragment.this).submitQuery(searchText);
                        }
                    });
                    return true;
                }
            });

            // Add an expand listener
            MenuItemCompat.setOnActionExpandListener(searchMenu, new HeroStatsFragment.DefaultOnActionExpandListener());
        }


    }

    public static class DefaultOnActionExpandListener implements MenuItemCompat.OnActionExpandListener {
        @Override
        public boolean onMenuItemActionExpand(final MenuItem item) {
            return true; // Return true to expand action view
        }

        @Override
        public boolean onMenuItemActionCollapse(final MenuItem item) {
            return true; // Return true to collapse action view
        }
    }

}
