package com.michaelfotiadis.mobiledota2.ui.core.base.fragment;

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

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.error.Error;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.hero.HeroStatsFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;

import butterknife.ButterKnife;

public abstract class BaseRecyclerFragment<D> extends BaseFragment {

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof Searchable) {
            setHasOptionsMenu(true);
        }
    }


    protected abstract RecyclerManager<D> getRecyclerManager();

    protected abstract RecyclerView getRecyclerView();

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
                    getRecyclerView().getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                        @Override
                        public void onAnimationsFinished() {
                            ((Searchable) BaseRecyclerFragment.this).submitQuery(query);
                        }
                    });

                    return true;
                }

                @Override
                public boolean onQueryTextChange(final String searchText) {
                    getRecyclerView().getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
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

    protected void setRecyclerError(final Error error) {

        final UiDataLoadError uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), error);
        if (uiDataLoadError.isRecoverable()) {

            if (uiDataLoadError.getKind() == UiDataLoadError.ErrorKind.NO_NETWORK) {
                showNoNetworkMessage();
            }
            getRecyclerManager().setError(uiDataLoadError.getMessage(), new QuoteOnClickListenerWrapper(R.string.label_try_again, new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    loadData();
                }
            }));

        } else {
            getRecyclerManager().setError(uiDataLoadError.getMessage());
        }

    }

    protected void showNoUserError() {
        final QuoteOnClickListenerWrapper listenerWrapper = new QuoteOnClickListenerWrapper(R.string.error_label_go_to_login, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getIntentDispatcher().openLoginActivity(v);
            }
        });
        getRecyclerManager().setError(getString(R.string.error_no_user), listenerWrapper);
    }

    protected abstract void initRecyclerManager(View view);

    protected abstract void loadData();

    private boolean isSearchable() {
        return this instanceof Searchable;
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
