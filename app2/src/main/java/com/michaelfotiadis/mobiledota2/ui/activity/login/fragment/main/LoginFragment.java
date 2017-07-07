package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.main;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.loader.error.ErrorKind;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadErrorFactory;
import com.michaelfotiadis.mobiledota2.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.ui.activity.login.LoginNavigationCommand;
import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import javax.inject.Inject;

public class LoginFragment extends BaseFragment implements LoginActionCallbacks {

    @Inject
    JobScheduler mJobScheduler;
    private LoginPresenter mLoginPresenter;

    @Override
    public void login(@NonNull final String username) {
        hideKeyboard();
        mLoginPresenter.showProgress();
        Answers.getInstance().logCustom(new CustomEvent("Attempted login as user"));
        mJobScheduler.startFetchPlayersJob(username, false);
    }

    @Override
    public void showLoginHelp(final View view) {
        if (getActivity() instanceof LoginNavigationCommand) {
            ((LoginNavigationCommand) getActivity()).showHelp(view);
        }
    }

    @Override
    public void showSteamLogin(final View view) {
        if (getActivity() instanceof LoginNavigationCommand) {
            ((LoginNavigationCommand) getActivity()).showSteamLogin(view);
        }
    }

    @Override
    public void onPopularSelected(final View view) {
        if (getActivity() instanceof LoginNavigationCommand) {
            ((LoginNavigationCommand) getActivity()).onNavigateToPopular(view);
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventLifecycleListener().enable();
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Login")
                .putContentType("Screen"));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginPresenter = new LoginPresenter(new LoginViewHolder(view), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.title_sign_in));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoginPresenter = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedPlayersEvent event) {

        if (event.getError() != null) {
            AppLog.w("Showing error " + event.getError());
            mLoginPresenter.showLogin();
            final UiDataLoadError uiDataLoadError;
            //noinspection IfMayBeConditional
            if (event.getError().getKind() == ErrorKind.NO_CONTENT_RETURNED) {
                uiDataLoadError = new UiDataLoadError(getString(R.string.error_no_players_found), UiDataLoadError.ErrorKind.NO_DATA, true);
            } else {
                uiDataLoadError = UiDataLoadErrorFactory.createError(getContext(), event.getError());
            }

            mLoginPresenter.setError(uiDataLoadError);
            if (uiDataLoadError.getKind() == UiDataLoadError.ErrorKind.NO_NETWORK) {
                showNoNetworkMessage();
            }

        } else if (event.getPlayers() == null || event.getPlayers().isEmpty()) {
            AppLog.w("No players found");
            Answers.getInstance().logCustom(new CustomEvent("Login - No users found for search"));
            mLoginPresenter.showLogin();
            mLoginPresenter.setError(new UiDataLoadError(getString(R.string.error_no_players_found), UiDataLoadError.ErrorKind.NO_DATA, true));
        } else {
            Answers.getInstance().logCustom(new CustomEvent("Login - Successfully fetched users for login"));
            AppLog.d(String.format(Locale.UK, "Showing result for %d players", event.getPlayers().size()));

            if (getActivity() instanceof LoginNavigationCommand) {
                ((LoginNavigationCommand) getActivity()).showPlayers(event.getPlayers());
            }

            mLoginPresenter.showLogin();

        }

    }

    public static BaseFragment newInstance() {
        return new LoginFragment();
    }


}
