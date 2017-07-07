package com.michaelfotiadis.mobiledota2.ui.core.base.error.errorpage;

/**
 *
 */
public interface QuotePageController {
    void display(final CharSequence message);

    void display(CharSequence message, QuoteOnClickListenerWrapper listenerWrapper);
}
