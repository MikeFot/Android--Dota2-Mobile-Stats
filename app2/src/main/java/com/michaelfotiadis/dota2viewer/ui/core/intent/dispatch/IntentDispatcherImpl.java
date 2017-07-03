package com.michaelfotiadis.dota2viewer.ui.core.intent.dispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.core.intent.NavLog;
import com.michaelfotiadis.dota2viewer.ui.core.intent.chromecustomtabs.CustomTabActivityHelper;
import com.michaelfotiadis.dota2viewer.ui.core.intent.factory.IntentFactory;
import com.michaelfotiadis.dota2viewer.ui.core.intent.factory.IntentFactoryImpl;


/**
 *
 */
public class IntentDispatcherImpl implements IntentDispatcher {

    private final IntentDispatcherInternal mDispatcher;
    private final IntentFactory mIntentFactory;

    public IntentDispatcherImpl(@NonNull final Context context) {
        this.mIntentFactory = new IntentFactoryImpl(context);
        this.mDispatcher = new IntentDispatcherInternal(context);
    }

    @Override
    public IntentFactory getIntentFactory() {
        return mIntentFactory;
    }

    @Override
    public void open(final View source, final Uri uri) {
        final Context context = mDispatcher.getContext();
        if (ChromeCustomTabFactory.canOpen(context, uri)) {
            openUrlWithChromeCustomTabs((Activity) context, source, uri);
        } else {
            openUrlDefault(source, uri);
        }
    }

    private void openUrlDefault(final View source, final Uri uri) {
        NavLog.d("Opening Uri: " + uri);
        final Intent intent = mIntentFactory.getOpenIntent(uri);
        mDispatcher
                .withView(source)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .dispatch(intent);
    }

    private void openUrlWithChromeCustomTabs(final Activity activity, final View source, final Uri uri) {
        NavLog.d("Opening Uri as ChromeTab: " + uri);
        final CustomTabsIntent intent = ChromeCustomTabFactory.create(activity);

        CustomTabActivityHelper.openCustomTab(activity, intent, uri,
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(final Activity activity, final Uri uri) {
                        // This is our fallback
                        openUrlDefault(source, uri);
                    }
                });
    }


    @Override
    public void dispatch(final View source, final Intent intent) {
        mDispatcher
                .withView(source)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .dispatch(intent);
    }

    @Override
    public void dispatchForResult(final View source, final Intent intent, final Integer requestCode) {
        mDispatcher
                .withView(source)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .forResult(requestCode)
                .dispatch(intent);
    }


    @Override
    public void openMainActivity(final View view) {
        mDispatcher
                .withView(view)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .dispatch(mIntentFactory.getMainIntent());
    }


    @Override
    public void openMarketRate(final View view) {
        mDispatcher
                .withView(view)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .dispatch(mIntentFactory.getMarketRateIntent());
    }

    @Override
    public void openLoginActivity(final View view) {
        mDispatcher
                .withView(view)
                .withAnimation(ActivityAnimation.SLIDE_IN_FROM_LEFT)
                .dispatch(mIntentFactory.getLoginIntent());
    }

    @Override
    public void openMatchDetailsActivity(final View view, final MatchContainer match) {
        mDispatcher
                .withView(view)
                .withAnimation(ActivityAnimation.SCALE_UP_FROM_VIEW)
                .dispatch(mIntentFactory.getMatchDetailsIntent(match));
    }

    @Override
    public void openPerformanceActivity() {
        mDispatcher
                .withAnimation(ActivityAnimation.SLIDE_UP_FROM_BOTTOM)
                .dispatch(mIntentFactory.getPerformanceIntent());
    }

    @Override
    public void showSystemWirelessSettings() {
        mDispatcher.dispatch(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }


    private void openWithSharedElementTransition(final Intent intent,
                                                 final View view,
                                                 final String transitionName,
                                                 final ActivityAnimation activityAnimation) {

        mDispatcher
                .withView(view)
                .withTransitionName(transitionName)
                .withAnimation(activityAnimation)
                .dispatch(intent);

    }

    private void openWithSharedElementTransition(final Intent intent,
                                                 final int requestCode,
                                                 final View view,
                                                 final String transitionName,
                                                 final ActivityAnimation activityAnimation) {

        mDispatcher
                .forResult(requestCode)
                .withView(view)
                .withTransitionName(transitionName)
                .withAnimation(activityAnimation)
                .dispatch(intent);

    }

}
