package com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final View mRoot;

    protected BaseRecyclerViewHolder(final View view) {
        super(view);
        mRoot = view;
        ButterKnife.bind(this, view);
    }

    public View getRoot() {
        return mRoot;
    }

}