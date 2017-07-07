package com.michaelfotiadis.mobiledota2.ui.view.utils;

import android.content.Context;

public final class RecyclerUtils {

    private RecyclerUtils() {
        // NOOP
    }

    public static int getColumnsForScreen(final Context context) {
        if (DeviceFormUtils.isLandscape(context)) {
            return DeviceFormUtils.isSw720dp(context) || DeviceFormUtils.isSw600dp(context) ? 3 : 2;
        } else if (DeviceFormUtils.isSw600dp(context)) {
            return 2;
        } else {
            return 1;
        }
    }

}
