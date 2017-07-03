package com.michaelfotiadis.dota2viewer.ui.core.intent.dispatch;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.core.intent.factory.IntentFactory;

public interface IntentDispatcher {
    IntentFactory getIntentFactory();

    void open(View source, Uri uri);

    void dispatch(View source, Intent intent);

    void dispatchForResult(View source, Intent intent, Integer requestCode);

    void openMainActivity(View view);

    void openMarketRate(View view);

    void openLoginActivity(View view);

    void openMatchDetailsActivity(View view, MatchContainer match);

    void openPerformanceActivity();

    void showSystemWirelessSettings();
}
