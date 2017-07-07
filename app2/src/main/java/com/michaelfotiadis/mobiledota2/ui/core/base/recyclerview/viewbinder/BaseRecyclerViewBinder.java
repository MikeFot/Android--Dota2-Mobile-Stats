package com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;
import com.michaelfotiadis.mobiledota2.ui.view.utils.ViewUtils;

public abstract class BaseRecyclerViewBinder<VH extends BaseRecyclerViewHolder, D> {

    private final Context mContext;

    protected BaseRecyclerViewBinder(final Context context) {
        mContext = context;
    }

    public final void bind(final VH holder, final D item) {
        reset(holder);
        setData(holder, item);
    }

    protected String getString(@StringRes final int resId) {
        return getContext().getString(resId);
    }

    @ColorInt
    protected int getColor(@ColorRes final int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    protected Drawable getDrawable(@DrawableRes final int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    protected Context getContext() {
        return mContext;
    }

    protected abstract void reset(final VH holder);

    protected abstract void setData(final VH holder, final D item);

    @SuppressWarnings("MethodMayBeStatic")
    protected void showView(final View view, final boolean show) {
        ViewUtils.showView(view, show);
    }
}