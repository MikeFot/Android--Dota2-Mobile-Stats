package com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage;

import android.support.annotation.StringRes;
import android.view.View;

/**
 *
 */
public class QuoteOnClickListenerWrapper {

    private final View.OnClickListener mListener;
    @StringRes
    private final int mResId;


    /**
     * Constructor which supports a resource id for the message to be displayed on the button
     *
     * @param resId    int resource ID of the message to be used
     * @param listener {@link View.OnClickListener} listener
     */
    public QuoteOnClickListenerWrapper(@StringRes final int resId, final View.OnClickListener listener) {
        this.mResId = resId;
        this.mListener = listener;
    }

    public View.OnClickListener getListener() {
        return mListener;
    }

    @StringRes
    public int getResId() {
        return mResId;
    }
}
