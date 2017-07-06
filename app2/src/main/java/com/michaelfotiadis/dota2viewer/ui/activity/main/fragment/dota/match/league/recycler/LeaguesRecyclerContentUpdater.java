package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.recycler;

import android.support.v7.widget.RecyclerView;

import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.EndlessRecyclerOnScrollListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.ListUtils;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handler for intelligently updating a {@link RecyclerView}.
 */
public class LeaguesRecyclerContentUpdater {

    private static final boolean DEBUG_ENABLED = true;

    private static final int CHUNK_SIZE = 20;

    private final List<League> mPendingItems;
    private final List<List<League>> mListOfLists;

    private final RecyclerView mRecyclerView;
    private final RecyclerManager<League> mRecyclerManager;

    public LeaguesRecyclerContentUpdater(final RecyclerManager<League> recyclerManager,
                                         final RecyclerView recyclerView,
                                         final RecyclerView.LayoutManager layoutManager) {
        mRecyclerManager = recyclerManager;

        mPendingItems = new ArrayList<>();
        mListOfLists = new ArrayList<>();

        mRecyclerView = recyclerView;

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page) {
                addPage(page);
            }
        });

    }

    /**
     * Attempts to add a page to the adapter
     *
     * @param page number of the current page
     */
    private void addPage(final int page) {
        if (mListOfLists.isEmpty()) {
            log("No more pages to add to recycler for page " + page);
        } else {

            /*
             *  post this on the view's thread to avoid a {@link java.util.ConcurrentModificationException}
             */
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    final int index = 0; // always use the first index

                    mRecyclerManager.addItems(mListOfLists.get(index));
                    mListOfLists.remove(index);
                    log(String.format(
                            Locale.UK,
                            "On Load More page %d : Recycler now has %d items and we still have %d chunks to add",
                            page,
                            mRecyclerManager.getItemCount(),
                            mListOfLists.size()));
                }
            });
        }
    }

    public void setItems(final List<League> items) {

        // if we get the same number of items, it's safe to assume that the list has not changed
        if (items.isEmpty() || items.size() != mPendingItems.size()) {

            mPendingItems.clear();
            mPendingItems.addAll(items);

            log("List is different and will update with " + mPendingItems.size() + " items");

            mListOfLists.clear();
            mListOfLists.addAll(ListUtils.chunk(mPendingItems, CHUNK_SIZE));

            if (mListOfLists.isEmpty()) {
                mRecyclerManager.setItems(mPendingItems);
            } else {
                mRecyclerManager.setItems(mListOfLists.get(0));
                mListOfLists.remove(0);
            }


            mRecyclerManager.updateUiState();
            log("Adapter now has " + mRecyclerManager.getItemCount() + " items");
        } else {
            log("Got the same number of items - will not update recycler items");
        }

    }

    private static void log(final String message) {

        if (DEBUG_ENABLED) {
            AppLog.d("Leagues Updater: " + message);
        }

    }

}
