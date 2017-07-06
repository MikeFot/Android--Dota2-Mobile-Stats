package com.michaelfotiadis.dota2viewer.ui.core.toast;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;


public final class SnackbarNotificationController implements ActivityNotificationController {

    private static final int COORDINATE_LAYOUT_ID = R.id.coordinatorLayout;
    private final View mCoordinateLayout;
    private static final int CHARS_PER_LINE = 20;
    @ColorRes
    private static final int COLOR_ALERT = R.color.error;
    @ColorRes
    private static final int COLOR_INFO = R.color.accent;
    @ColorRes
    private static final int COLOR_WHITE = R.color.md_white_1000;

    public SnackbarNotificationController(final Activity activity) {
        this(activity.findViewById(COORDINATE_LAYOUT_ID));
    }

    private SnackbarNotificationController(final View view) {
        this.mCoordinateLayout = view;
        if (mCoordinateLayout == null) {
            AppLog.w(this.getClass().getSimpleName() + " instantiated with invalid view");
        }
    }

    @Override
    public void showNotification(final CharSequence message, final CharSequence actionText, final View.OnClickListener listener) {

        final Snackbar bar = createSnackBar(message, actionText, listener);

        bar.show();
    }

    @Override
    public void showAlert(final CharSequence message) {
        this.showAlert(message, null, null);
    }

    @Override
    public void showAlert(final CharSequence message, final CharSequence actionText, final View.OnClickListener listener) {

        final Snackbar bar = createSnackBar(message, actionText, listener);

        bar.getView().setBackgroundColor(getColor(COLOR_ALERT));

        getMessageText(bar).setTextColor(getColor(COLOR_WHITE));
        getActionText(bar).setTextColor(getColor(COLOR_WHITE));

        bar.show();
    }

    @Override
    public void showAlert(@StringRes final int message, @StringRes final int actionText, final View.OnClickListener listener) {
        this.showAlert(getString(message), getString(actionText), listener);
    }

    @Override
    public void showInfo(final CharSequence message) {
        this.showInfo(message, null, null);
    }

    @Override
    public void showInfo(final CharSequence message,
                         final CharSequence actionText,
                         final View.OnClickListener listener) {

        final Snackbar bar = createSnackBar(message, actionText, listener);

        bar.getView().setBackgroundColor(getColor(COLOR_INFO));

        getMessageText(bar).setTextColor(getColor(COLOR_WHITE));

        if (TextUtils.isNotEmpty(actionText)) {
            getActionText(bar).setTextColor(getColor(COLOR_WHITE));
        }

        bar.show();
    }

    @Override
    public void showInfo(@StringRes final int message, @StringRes final int actionText, @Nullable final View.OnClickListener listener) {
        this.showInfo(getString(message), getString(actionText), listener);
    }

    private Snackbar createSnackBar(final CharSequence message, final CharSequence actionText, @Nullable final View.OnClickListener listener) {
        final Snackbar bar = Snackbar.make(mCoordinateLayout, message, getDuration(listener != null));

        bar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                bar.dismiss();
            }
        });

        adjustMaxLines(bar);

        if (listener != null) {
            bar.setAction(actionText, listener);
        }
        return bar;
    }

    @Override
    public void showNotification(final CharSequence message) {
        this.showNotification(message, null, null);
    }

    @Override
    public void showNotification(@StringRes final int message,
                                 @StringRes final int actionText,
                                 final View.OnClickListener listener) {
        this.showNotification(getString(message), getString(actionText), listener);

    }

    @Override
    public void showNotification(@StringRes final int message) {
        this.showNotification(message, 0, null);
    }

    private static int getDuration(final boolean hasListener) {
        return hasListener ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG;
    }

    private static TextView getMessageText(final Snackbar bar) {
        return (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
    }

    private static TextView getActionText(final Snackbar bar) {
        return (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_action);
    }

    private static void adjustMaxLines(final Snackbar bar) {
        final TextView textView = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);

        final int maxLines = textView.getText().length() / CHARS_PER_LINE == 0 ? 1 : textView.getText().length() / CHARS_PER_LINE;

        textView.setMaxLines(maxLines);
    }

    @Nullable
    private String getString(@StringRes final int stringId) {
        final String retVal;

        if (stringId > 0) {
            retVal = mCoordinateLayout.getContext().getString(stringId);
        } else {
            retVal = null;
        }

        return retVal;
    }

    @ColorInt
    private int getColor(@ColorRes final int colorId) {
        return ContextCompat.getColor(mCoordinateLayout.getContext(), colorId);
    }

}
