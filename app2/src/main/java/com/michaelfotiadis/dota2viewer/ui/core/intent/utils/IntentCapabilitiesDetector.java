package com.michaelfotiadis.dota2viewer.ui.core.intent.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import java.util.List;

/**
 *
 */

public final class IntentCapabilitiesDetector {

    private IntentCapabilitiesDetector() {
        // NOOP
    }

    public static boolean canHandle(@NonNull final Context context,
                                    @NonNull final Intent intent) {

        final List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);

        return list != null && !list.isEmpty();

    }

}
