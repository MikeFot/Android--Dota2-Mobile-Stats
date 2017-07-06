package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Taken from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private final LinearLayoutManager mLinearLayoutManager;
    private int mPreviousTotal = 0; // The total number of items in the data set after the last load
    private boolean mIsLoading = true; // True if we are still waiting for the last set of data to load.
    @SuppressWarnings("FieldCanBeLocal")
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
    private int mCurrentPage = 0;

    public EndlessRecyclerOnScrollListener(final RecyclerView.LayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = (LinearLayoutManager) linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (recyclerView.isComputingLayout()) {
            return;
        }

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLinearLayoutManager.getItemCount();
        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mIsLoading && mTotalItemCount > mPreviousTotal) {
            mIsLoading = false;
            mPreviousTotal = mTotalItemCount;
        }

        if (!mIsLoading && (mTotalItemCount - mVisibleItemCount)
                <= (mFirstVisibleItem + VISIBLE_THRESHOLD)) {
            // End has been reached

            // Do something
            mCurrentPage++;

            onLoadMore(mCurrentPage);

            mIsLoading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}