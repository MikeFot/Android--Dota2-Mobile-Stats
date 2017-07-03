package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchDetailsItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchErrorItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchListItem;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MatchRecyclerAdapter extends BaseRecyclerViewAdapter<MatchListItem, BaseRecyclerViewHolder> {

    private static final int TYPE_OVERVIEW = 0;
    private static final int TYPE_DETAILS = 1;
    private static final int TYPE_ERROR = 2;
    private static final int TYPE_LOADING = 3;
    private final MatchDetailsViewBinder mDetailsBinder;
    private final MatchOverviewViewBinder mOverviewBinder;
    private final MatchErrorViewBinder mErrorBinder;
    private final MatchLoadingViewBinder mLoadingBinder;
    private final List<Hero> mDotaHeroes;
    private final List<GameItem> mDotaItems;


    public MatchRecyclerAdapter(final Context context,
                                final ImageLoader imageLoader,
                                final OnItemSelectedListener<MatchItem> listener) {
        super(context);
        setHasStableIds(true);
        mDotaHeroes = new ArrayList<>();
        mDotaItems = new ArrayList<>();
        mDetailsBinder = new MatchDetailsViewBinder(context, imageLoader, listener);
        mOverviewBinder = new MatchOverviewViewBinder(context, listener);
        mErrorBinder = new MatchErrorViewBinder(context, listener);
        mLoadingBinder = new MatchLoadingViewBinder(context);
    }

    void setDotaHeroes(final List<Hero> heroes) {
        mDotaHeroes.clear();
        mDotaHeroes.addAll(heroes);
        notifyDetailsChanged();
    }

    void setDotaItems(final List<GameItem> items) {
        mDotaItems.clear();
        mDotaItems.addAll(items);
        notifyDetailsChanged();
    }

    private int getPositionForItem(final long id) {
        int i = 0;
        for (final MatchListItem item : getItems()) {
            if (item.getItemId() == id) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void notifyDetailsChanged() {
        if (!mDotaHeroes.isEmpty() && !mDotaItems.isEmpty()) {
            int i = 0;
            for (final MatchListItem item : getItems()) {
                if (item.isDetailsItem()) {
                    notifyItemChanged(i);
                }
                i++;
            }
        }
    }

    @Nullable
    private MatchListItem getItemForId(final long id) {
        for (final MatchListItem item : getItems()) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    void setLoading(final long id) {
        synchronized (this) {
            final MatchListItem item = getItemForId(id);
            if (item != null) {
                final int position = getPositionForItem(item.getItemId());
                item.setMatchDetailsItem(null);
                item.setMatchErrorItem(null);
                notifyItemChanged(position);
            }
        }
    }

    public void addLoadingItem() {
        synchronized (this) {
            addItem(new MatchListItem(null));
        }
    }

    public void removeAllLoadingItems() {
        synchronized (this) {
            final ListIterator<MatchListItem> itemIterator = getItems().listIterator();

            while (itemIterator.hasNext()) {
                if (itemIterator.next().isLoadingItem()) {
                    itemIterator.remove();
                }
            }
        }
    }

    public void add(final long id, final UiDataLoadError error) {
        synchronized (this) {
            final MatchListItem item = getItemForId(id);
            if (item != null) {
                final int position = getPositionForItem(item.getItemId());
                item.setMatchErrorItem(new MatchErrorItem(id, error));
                notifyItemChanged(position);
            }
        }
    }

    public void add(final long playerId, final MatchDetails matchDetails) {
        synchronized (this) {
            final MatchListItem item = getItemForId(matchDetails.getMatchId());
            if (item != null) {
                final int position = getPositionForItem(item.getItemId());
                item.setMatchDetailsItem(new MatchDetailsItem(playerId, matchDetails, mDotaHeroes, mDotaItems));
                item.setMatchErrorItem(null);
                if (!mDotaHeroes.isEmpty() && !mDotaItems.isEmpty()) {
                    notifyItemChanged(position);
                }
            }
        }
    }

    @Override
    public void addItem(final MatchListItem item) {
        super.addItem(item);
    }

    @Override
    protected boolean isItemValid(final MatchListItem item) {
        return item != null;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        if (viewType == TYPE_DETAILS) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(MatchDetailsViewHolder.getLayoutId(), parent, false);
            return new MatchDetailsViewHolder(view);
        } else if (viewType == TYPE_ERROR) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(MatchErrorViewHolder.getLayoutId(), parent, false);
            return new MatchErrorViewHolder(view);
        } else if (viewType == TYPE_OVERVIEW) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(MatchOverviewViewHolder.getLayoutId(), parent, false);
            return new MatchOverviewViewHolder(view);
        } else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(MatchLoadingViewHolder.getLayoutId(), parent, false);
            return new MatchLoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {

        final MatchListItem item = getItem(position);

        if (holder instanceof MatchDetailsViewHolder) {
            mDetailsBinder.bind((MatchDetailsViewHolder) holder, item.getMatchDetailsItem());
        } else if (holder instanceof MatchErrorViewHolder) {
            mErrorBinder.bind((MatchErrorViewHolder) holder, item.getMatchErrorItem());
        } else if (holder instanceof MatchOverviewViewHolder) {
            mOverviewBinder.bind((MatchOverviewViewHolder) holder, item.getMatchOverviewItem());
        } else if (holder instanceof MatchLoadingViewHolder) {
            mLoadingBinder.bind((MatchLoadingViewHolder) holder, item);
        } else {
            AppLog.e("Binder not found!");
        }

    }

    @Override
    public int getItemViewType(final int position) {

        final MatchListItem item = getItem(position);

        if (item.isErrorItem()) {
            return TYPE_ERROR;
        } else if (item.isDetailsItem() && !mDotaItems.isEmpty() && !mDotaHeroes.isEmpty()) {
            return TYPE_DETAILS;
        } else if (item.isOverviewItem() || item.isDetailsItem()) {
            return TYPE_OVERVIEW;
        } else {
            return TYPE_LOADING;
        }

    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).getItemId();
    }

    List<Hero> getDotaHeroes() {
        return mDotaHeroes;
    }

    List<GameItem> getDotaItems() {
        return mDotaItems;
    }

}
