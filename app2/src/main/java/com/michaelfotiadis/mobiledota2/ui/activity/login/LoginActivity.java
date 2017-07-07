package com.michaelfotiadis.mobiledota2.ui.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.main.LoginFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.PopularPlayersFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.PlayerPickerFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.web.WebViewFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
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
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Login Activity")
                .putContentType("Screen"));
        AppLog.d("Starting login activity");
        setTitle(getString(R.string.title_login));

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
    public void showSteamLogin(final View view) {
        Answers.getInstance().logCustom(new CustomEvent("Navigated to Steam Login"));
        replaceContentFragment(WebViewFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
    }

    @Override
    public void showHelp(final View view) {
        // TODO implement this!!!!
    }

    @Override
    public void onNavigateToPopular(final View view) {
        Answers.getInstance().logCustom(new CustomEvent("Navigated to Popular Player Login"));
        replaceContentFragment(PopularPlayersFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            setResult(Activity.RESULT_CANCELED);
            Answers.getInstance().logCustom(new CustomEvent("Exited Login without selecting"));
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }


}
