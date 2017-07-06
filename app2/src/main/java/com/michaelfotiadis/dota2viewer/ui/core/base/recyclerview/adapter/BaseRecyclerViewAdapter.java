package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.michaelfotiadis.dota2viewer.utils.AppLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<D, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final Context mContext;
    private final List<D> mItems = Collections.synchronizedList(new ArrayList<D>());
    private boolean bDataAdditionAttempted = false;
    private OnItemsChangedListener mListener;

    protected BaseRecyclerViewAdapter(final Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void addItem(final D item, final int position) {
        synchronized (this) {
            bDataAdditionAttempted = true;
            if (isItemValid(item)) {
                mItems.add(position, item);
                this.notifyItemInserted(position);
                this.callItemsChangedListener();
            } else {
                AppLog.w("Not adding item in recycler because its invalid. Type: '" + getClassName(item) + "'");
            }
        }
    }

    public void addItem(final D item) {
        addItem(item, mItems.size());
    }

    public void addItems(final Collection<D> items) {

        synchronized (this) {

            bDataAdditionAttempted = true;
            for (final D item : items) {
                addItem(item);
            }

        }

    }

    private void callItemsChangedListener() {
        if (mListener != null) {
            mListener.onItemsChanged();
        }
    }

    public void clearItems() {
        mItems.clear();
        this.notifyDataSetChanged();
        callItemsChangedListener();
    }

    public List<D> getItems() {
        return mItems;
    }

    public void setItems(final List<D> items) {
        AppLog.d("Will try to SET '" + items.size() + "' items of type " + getClassName(items));

        final List<D> filteredList = new ArrayList<>();

        for (final D item : items) {
            if (isItemValid(item)) {
                filteredList.add(item);
            } else {
                AppLog.w("Not adding item in recycler because its invalid. Type: '" + getClassName(item) + "'");
            }
        }

        AppLog.d("Actually setting '" + filteredList.size() + "' items of type " + getClassName(items));

        bDataAdditionAttempted = true;
        mItems.clear();
        mItems.addAll(filteredList);
        this.notifyDataSetChanged();
        callItemsChangedListener();
    }

    public D getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public boolean hasAttemptedDataAddition() {
        return bDataAdditionAttempted;
    }

    protected abstract boolean isItemValid(final D item);

    public void removeItem(final int position) {
        mItems.remove(position);
        this.notifyItemRemoved(position);
        this.callItemsChangedListener();
    }

    public void removeItem(final D item) {
        final int position = mItems.indexOf(item);
        if (position > -1) {
            removeItem(position);
        }
    }

    public void setOnItemsChangedListener(final OnItemsChangedListener listener) {
        this.mListener = listener;
    }

    private static String getClassName(final List<?> list) {
        return list.isEmpty() ? "<unknown>" : getClassName(list.get(0));
    }

    private static String getClassName(final Object object) {
        return object.getClass().getSimpleName();
    }

    public interface OnItemsChangedListener {
        void onItemsChanged();
    }
}