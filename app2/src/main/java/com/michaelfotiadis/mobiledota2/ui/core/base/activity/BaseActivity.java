package com.michaelfotiadis.mobiledota2.ui.core.base.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.event.listener.EventLifecycleListener;
import com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch.IntentDispatcher;
import com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch.IntentDispatcherImpl;
import com.michaelfotiadis.mobiledota2.ui.core.toast.ActivityNotificationController;
import com.michaelfotiadis.mobiledota2.ui.core.toast.SnackbarNotificationController;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    protected static final int NO_LAYOUT = 0;
    private LifecycleRegistry mLifecycleRegistry;
    private EventLifecycleListener mEventLifecycleListener;
    private ActivityNotificationController mNotificationController;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        if (getLayoutResId() != NO_LAYOUT) {
            setContentView(getLayoutResId());
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            } else {
                AppLog.w("No toolbar");
            }
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            if (coordinatorLayout != null) {
                mNotificationController = new SnackbarNotificationController(this);
            }
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    public void setTitle(final CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        } else {
            super.setTitle(title);
        }
    }

    protected void addContentFragmentIfMissing(final Fragment fragment, final int id, final String fragmentTag) {

        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) == null) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(id, fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    protected void replaceContentFragment(final Fragment fragment, final int id, final String fragmentTag) {

        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) == null) {
            addContentFragmentIfMissing(fragment, id, fragmentTag);
        } else {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(id, fragment, fragmentTag);
            fragmentTransaction.commit();
        }


    }

    protected EventLifecycleListener getEventLifecycleListener() {
        if (mEventLifecycleListener == null) {
            mEventLifecycleListener = new EventLifecycleListener<>(this, getLifecycle());
        }
        return mEventLifecycleListener;
    }

    protected void setDisplayHomeAsUpEnabled(final boolean isEnabled) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected IntentDispatcher getIntentDispatcher() {
        return new IntentDispatcherImpl(this);
    }

    protected String getCurrentUserId() {
        return new UserPreferences(this).getCurrentUserId();
    }

    public ActivityNotificationController getNotificationController() {
        return mNotificationController;
    }

}
