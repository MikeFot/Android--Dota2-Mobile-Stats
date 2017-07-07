package com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement;

import android.view.View;

import com.michaelfotiadis.mobiledota2.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;

/**
 *
 */
public interface UiStateKeeper {

    View getContentView();

    View getEmptyView();

    View getErrorView();

    View getProgressView();

    void showContent();

    void showEmpty();

    void showEmpty(CharSequence message);

    void showError(final CharSequence message);

    void showError(final CharSequence message, final QuoteOnClickListenerWrapper listenerWrapper);

    void showProgress();
}
