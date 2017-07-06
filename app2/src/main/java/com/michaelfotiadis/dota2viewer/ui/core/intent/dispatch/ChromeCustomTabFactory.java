package com.michaelfotiadis.dota2viewer.ui.core.intent.dispatch;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.utils.AppLog;


/*package*/ class ChromeCustomTabFactory {
    public static CustomTabsIntent create(final Activity activity) {
        final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ResourcesCompat.getColor(activity.getResources(), R.color.primary_dark, null));
        builder.setStartAnimations(
                activity,
                R.anim.slide_in_right_chrome,
                R.anim.slide_out_left_chrome);
        builder.setExitAnimations(
                activity,
                R.anim.slide_in_left_chrome,
                R.anim.slide_out_right_chrome);
        return builder.build();
    }

    public static boolean canOpen(final Context context, final Uri uri) {
        final boolean retVal;
        final String scheme = uri.getScheme();

        if ("https".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme)) {
            retVal = context instanceof Activity
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        } else {
            retVal = false;
        }

        AppLog.d("Openable with ChromeTabs='" + retVal + "', scheme='" + scheme + "', url='" + uri + "'");
        return retVal;
    }
}
