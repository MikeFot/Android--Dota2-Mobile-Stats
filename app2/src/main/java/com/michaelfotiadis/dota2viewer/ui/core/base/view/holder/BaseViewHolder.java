package com.michaelfotiadis.dota2viewer.ui.core.base.view.holder;


import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Base View Holder class
 */
public abstract class BaseViewHolder {

    private final View mRoot;

    protected BaseViewHolder(final View view) {
        this.mRoot = view;
        ButterKnife.bind(this, view);
    }

    public View getRoot() {
        return mRoot;
    }

    public Context getContext() {
        return mRoot.getContext();
    }

}
