package com.michaelfotiadis.mobiledota2.ui.view.utils;

import android.content.Context;

import com.michaelfotiadis.mobiledota2.R;

/**
 */
public final class DeviceFormUtils {

    private DeviceFormUtils() {
        // NOOP
    }

    public static boolean isTablet(final Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isLandscape(final Context context) {
        return context.getResources().getBoolean(R.bool.isLandscape);
    }

    public static boolean isSw600dp(final Context context) {
        return context.getResources().getBoolean(R.bool.is600);
    }

    public static boolean isSw720dp(final Context context) {
        return context.getResources().getBoolean(R.bool.is720);
    }
}
