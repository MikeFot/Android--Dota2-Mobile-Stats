package com.michaelfotiadis.dota2viewer.ui.core.base.view.binder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.michaelfotiadis.dota2viewer.ui.core.base.view.holder.BaseViewHolder;

public abstract class BaseViewBinder<VH extends BaseViewHolder, D> {

    private final Context mContext;

    protected BaseViewBinder(final Context context) {
        mContext = context;
    }

    public final void bind(final VH holder, final D item) {
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

    protected abstract void setData(final VH holder, final D item);

}