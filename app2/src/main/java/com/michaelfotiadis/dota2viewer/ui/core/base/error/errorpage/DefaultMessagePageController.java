package com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class DefaultMessagePageController implements QuotePageController {

    @BindView(R.id.error_button)
    protected Button mActionButton;
    @BindView(R.id.error_message)
    protected TextView mErrorDescription;

    public DefaultMessagePageController(final View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void display(final CharSequence message) {
        display(message, null);
    }

    @Override
    public void display(final CharSequence message, final QuoteOnClickListenerWrapper listenerWrapper) {
        mErrorDescription.setText(message);

        if (listenerWrapper == null || listenerWrapper.getListener() == null) {
            mActionButton.setVisibility(View.GONE);
        } else {
            mActionButton.setVisibility(View.VISIBLE);
            // set up the action button label
            if (listenerWrapper.getResId() == 0) {
                mActionButton.setText(R.string.label_try_again);
            } else {
                mActionButton.setText(listenerWrapper.getResId());
            }
            mActionButton.setOnClickListener(listenerWrapper.getListener());
        }

    }

}
