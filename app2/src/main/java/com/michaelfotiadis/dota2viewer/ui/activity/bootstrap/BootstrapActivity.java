package com.michaelfotiadis.dota2viewer.ui.activity.bootstrap;

import android.os.Bundle;

import com.michaelfotiadis.dota2viewer.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.dota2viewer.utils.AppLog;

public class BootstrapActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.d("Starting Bootstrap Activity");
        openMainActivity();
    }

    @Override
    protected int getLayoutResId() {
        return NO_LAYOUT;
    }

    private void openMainActivity() {
        getIntentDispatcher().openMainActivity(null);
    }

    private void openLoginActivity() {
        getIntentDispatcher().openLoginActivity(null);
    }


}
