package com.michaelfotiadis.dota2viewer.ui.core.toast;

import android.support.annotation.StringRes;
import android.view.View;

public interface ActivityNotificationController {

    void showNotification(final CharSequence message, final CharSequence actionText, final View.OnClickListener listener);

    void showAlert(CharSequence message);

    void showAlert(CharSequence message, CharSequence actionText, View.OnClickListener listener);

    void showAlert(int message, int actionText, View.OnClickListener listener);

    void showInfo(CharSequence message);

    void showInfo(CharSequence message, CharSequence actionText, View.OnClickListener listener);

    void showInfo(@StringRes int message, @StringRes int actionText, View.OnClickListener listener);

    void showNotification(final CharSequence message);

    void showNotification(final int message, final int actionText, final View.OnClickListener listener);

    void showNotification(final int message);
}
