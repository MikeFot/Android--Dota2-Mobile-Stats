package com.michaelfotiadis.mobiledota2.ui.core.intent.factory;

import android.content.Intent;
import android.net.Uri;

import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchContainer;

public interface IntentFactory {
    Intent getOpenIntent(Uri uri);

    Intent getMainIntent();

    Intent getLoginIntent();

    Intent getPerformanceIntent();

    Intent getMatchDetailsIntent(MatchContainer match);

    Intent getMarketRateIntent();

    Intent getHelpIntent();
}
