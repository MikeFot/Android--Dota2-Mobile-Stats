package com.michaelfotiadis.dota2viewer.ui.view.utils;

import android.content.Context;

import com.michaelfotiadis.dota2viewer.R;

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
