package com.michaelfotiadis.mobiledota2.ui.activity.performance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.CalendarFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.hero.HeroStatsFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

public class PerformanceActivity extends BaseActivity {

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final int CONTENT_ID = R.id.content_frame;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            AppLog.d("Clicked on ID" + item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    showFragment(CalendarFragment.newInstance());
                    return true;
                case R.id.navigation_heroes:
                    showFragment(HeroStatsFragment.newInstance());
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Performance Activity")
                .putContentType("Screen"));
        setTitle(getString(R.string.title_performance));
        setDisplayHomeAsUpEnabled(true);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_calendar);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_performance;
    }

    private void showFragment(final Fragment fragment) {
        replaceContentFragment(fragment, CONTENT_ID, FRAGMENT_TAG);
    }

    public static Intent newInstance(final Context context) {
        return new Intent(context, PerformanceActivity.class);
    }

}
