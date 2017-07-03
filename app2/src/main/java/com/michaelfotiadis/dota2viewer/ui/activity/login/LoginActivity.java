package com.michaelfotiadis.dota2viewer.ui.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.view.Gravity;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.main.LoginFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerPickerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.List;

public class LoginActivity extends BaseActivity implements LoginNavigationCommand {

    private static final int CONTENT_ID = R.id.content_frame;
    private static final String FRAGMENT_TAG = "fragment_tag";

    public static Intent newInstance(final Context context) {

        return new Intent(context, LoginActivity.class);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLog.d("Starting login activity");
        setTitle("Login");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addContentFragmentIfMissing(LoginFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_container;
    }

    @Override
    public void showPlayers(final List<PlayerSummary> playerSummaries) {

        final Fragment fragment = PlayerPickerFragment.newInstance(playerSummaries);

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(CONTENT_ID, fragment, FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Slide slideTransition = new Slide(Gravity.RIGHT);
            slideTransition.setDuration(500);
            fragment.setEnterTransition(slideTransition);
        }

        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }


}
