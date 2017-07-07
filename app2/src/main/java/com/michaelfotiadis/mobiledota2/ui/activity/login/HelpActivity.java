package com.michaelfotiadis.mobiledota2.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_help));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_help;
    }

    public static Intent newInstance(final Context context) {
        return new Intent(context, HelpActivity.class);
    }
}
