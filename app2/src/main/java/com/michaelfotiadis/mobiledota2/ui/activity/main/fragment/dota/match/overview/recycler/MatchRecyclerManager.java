package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler;

import android.support.annotation.Nullable;

import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model.MatchListItem;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import java.util.List;

public class MatchRecyclerManager extends RecyclerManager<MatchListItem> {

    public MatchRecyclerManager(final Builder<MatchListItem> builder) {
        super(builder);
    }

    public void setDotaHeroes(final List<Hero> heroes) {
        ((MatchRecyclerAdapter) mAdapter).setDotaHeroes(heroes);
    }

    public void setDotaItems(final List<GameItem> items) {
        ((MatchRecyclerAdapter) mAdapter).setDotaItems(items);
    }

    public void setLoading(final long id) {
        ((MatchRecyclerAdapter) mAdapter).setLoading(id);
    }

    public void add(final long id, final UiDataLoadError error) {
        ((MatchRecyclerAdapter) mAdapter).add(id, error);
    }

    public void add(final long playerId, final MatchDetails matchDetails) {
        ((MatchRecyclerAdapter) mAdapter).add(playerId, matchDetails);
    }

    public void addLoadingItem() {
        removeAllLoadingItems();
        ((MatchRecyclerAdapter) mAdapter).addLoadingItem();
    }

    public void removeAllLoadingItems() {
        ((MatchRecyclerAdapter) mAdapter).removeAllLoadingItems();
    }

    public boolean isLoadingMore() {
        if (mAdapter.getItemCount() == 0) {
            return false;
        } else {
            final MatchListItem lastItem = mAdapter.getItem(mAdapter.getItemCount() - 1);
            return lastItem.isLoadingItem();
        }
    }

    public boolean isLoading() {

        if (isLoadingMore()) {
            return true;
        } else if (mAdapter.getItems().isEmpty()) {
            return true;
        } else if (((MatchRecyclerAdapter) mAdapter).getDotaHeroes().isEmpty() ||
                ((MatchRecyclerAdapter) mAdapter).getDotaItems().isEmpty()) {
            return true;
        } else {

            for (final MatchListItem item : mAdapter.getItems()) {
                if (!item.isDetailsItem()) {
                    return true;
                }
            }

        }

        return false;
    }

    @Nullable
    public Long getLastValidId() {

        if (mAdapter.getItemCount() == 0) {
            return null;
        } else {
            final MatchListItem lastItem = mAdapter.getItem(mAdapter.getItemCount() - 1);
            if (lastItem.getMatchOverviewItem() != null) {
                return lastItem.getItemId();
            } else if (mAdapter.getItemCount() > 1) {
                final MatchListItem secondToLastItem = mAdapter.getItem(mAdapter.getItemCount() - 2);
                return secondToLastItem != null ? secondToLastItem.getItemId() : null;
            } else {
                return null;
            }

        }
    }

    public List<Hero> getDotaHeroes() {
        return ((MatchRecyclerAdapter) mAdapter).getDotaHeroes();
    }

    public List<GameItem> getDotaItems() {
        return ((MatchRecyclerAdapter) mAdapter).getDotaItems();
    }

}
