package com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage;

/**
 *
 */
public interface QuotePageController {
    void display(final CharSequence message);

    void display(CharSequence message, QuoteOnClickListenerWrapper listenerWrapper);
}
