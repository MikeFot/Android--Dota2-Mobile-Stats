package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager;

import android.support.v7.widget.RecyclerView;

import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.animation.DefaultAppItemAnimator;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class RecyclerManager<D> {

    protected final BaseRecyclerViewAdapter<D, ?> mAdapter;
    private final StateCoordinator mStateCoordinator;
    private final RecyclerView mRecyclerView;

    protected RecyclerManager(final Builder<D> builder) {

        this.mStateCoordinator = new StateCoordinator(
                builder.adapter,
                builder.stateKeeper,
                builder.emptyMessage);

        this.mAdapter = builder.adapter;
        this.mAdapter.setOnItemsChangedListener(
                new BaseRecyclerViewAdapter.OnItemsChangedListener() {
                    @Override
                    public void onItemsChanged() {
                        updateUiState();
                    }
                });

        this.mRecyclerView = builder.recycler;

        if (builder.animator == null) {
            builder.recycler.setItemAnimator(new DefaultAppItemAnimator());
        } else {
            builder.recycler.setItemAnimator(builder.animator);
        }

        builder.recycler.setAdapter(mAdapter);
    }

    public synchronized void addItem(final D item, final int position) {
        mAdapter.addItem(item, position);
    }

    public synchronized void addItem(final D item) {
        mAdapter.addItem(item);
    }

    public synchronized void addItems(final Collection<D> items) {
        mAdapter.addItems(items);
    }

    public void clearError() {
        mStateCoordinator.clearError();
    }

    public synchronized void clearItems() {
        mAdapter.clearItems();
    }

    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    public List<D> getItems() {
        return mAdapter.getItems();
    }

    public boolean hasHadDataAdded() {
        return mAdapter.hasAttemptedDataAddition();
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(final int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void removeItem(final int position) {
        mAdapter.removeItem(position);
    }

    public void removeItem(final D item) {
        mAdapter.removeItem(item);
    }

    public void setError(final CharSequence errorMessage) {
        setError(errorMessage, null);
    }

    public void setError(final CharSequence errorMessage,
                         final QuoteOnClickListenerWrapper listenerWrapper) {
        mStateCoordinator.setError(errorMessage, listenerWrapper);
    }

    public void setItems(final List<D> items) {
        mAdapter.setItems(items);
    }

    public void setAnimator(final RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void updateUiState() {
        mStateCoordinator.updateUiState();
    }

    public void updateUiState(final State state) {
        mStateCoordinator.updateUiState(state);
    }

    public static class Builder<D> {
        private final BaseRecyclerViewAdapter<D, ?> adapter;
        private RecyclerView.ItemAnimator animator = new DefaultAppItemAnimator();
        private CharSequence emptyMessage;
        private RecyclerView recycler;
        private UiStateKeeper stateKeeper;


        public Builder(final BaseRecyclerViewAdapter<D, ?> adapter) {
            this.adapter = adapter;
        }

        public RecyclerManager<D> build() {
            if (recycler == null) {
                throw new IllegalArgumentException("RecyclerView cannot be null");
            }

            if (adapter == null) {
                throw new IllegalArgumentException("RecyclerViewAdapter cannot be null");
            }

            return new RecyclerManager<>(this);
        }

        public Builder<D> setAnimator(final RecyclerView.ItemAnimator animator) {
            this.animator = animator;
            return this;
        }

        public Builder<D> setEmptyMessage(final CharSequence emptyMessage) {
            this.emptyMessage = emptyMessage;
            return this;
        }

        public Builder<D> setRecycler(final RecyclerView recycler) {
            this.recycler = recycler;
            return this;
        }

        public Builder<D> setStateKeeper(final UiStateKeeper stateKeeper) {
            this.stateKeeper = stateKeeper;
            return this;
        }
    }
}
