package com.michaelfotiadis.mobiledota2.ui.core.intent.factory;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchContainer;
import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchDetailsActivity;
import com.michaelfotiadis.mobiledota2.ui.activity.login.LoginActivity;
import com.michaelfotiadis.mobiledota2.ui.activity.main.MainActivity;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.PerformanceActivity;

import java.util.List;

/**
 *
 */
public class IntentFactoryImpl implements IntentFactory {

    private final Context mContext;

    public IntentFactoryImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Intent getOpenIntent(final Uri uri) {
        final Intent intent;
        if (uri == null) {
            intent = null;
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
        }
        return intent;
    }

    @Override
    public Intent getMainIntent() {
        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    @Override
    public Intent getLoginIntent() {
        return LoginActivity.newInstance(mContext);
    }

    @Override
    public Intent getPerformanceIntent() {
        return PerformanceActivity.newInstance(mContext);
    }

    @Override
    public Intent getMatchDetailsIntent(final MatchContainer match) {
        return MatchDetailsActivity.newInstance(mContext, match);
    }

    @Override
    public Intent getMarketRateIntent() {
        final Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = mContext.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (final ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                final ActivityInfo otherAppActivity = otherApp.activityInfo;
                final ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                return rateIntent;

            }
        }
        // if GP not present on device, open web browser
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
    }


}
