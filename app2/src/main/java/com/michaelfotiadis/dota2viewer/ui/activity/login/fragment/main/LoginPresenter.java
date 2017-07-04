package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.main;

import android.view.View;
import android.widget.ImageView;

import com.michaelfotiadis.dota2viewer.data.persistence.error.UiDataLoadError;
import com.michaelfotiadis.dota2viewer.ui.view.credentials.CredentialsInputLayout;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;

/*package*/ class LoginPresenter {

    private static final int INDEX_LOGIN = 0;
    private static final int INDEX_PROGRESS = 1;

    private final LoginViewHolder mViewHolder;
    private final LoginActionCallbacks mCallbacks;


    LoginPresenter(final LoginViewHolder viewHolder,
                   final LoginActionCallbacks callbacks) {

        mViewHolder = viewHolder;
        mCallbacks = callbacks;


        mViewHolder.getLoginButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                login(mViewHolder.getInputLayout().getUsername());
            }
        });

        mViewHolder.getInputLayout().setCredentialsInputListener(new CredentialsInputLayout.CredentialsInputListener() {
            @Override
            public void onKeypadSignIn(final String username) {
                login(username);
            }

            @Override
            public void onReadyStateChanged(final boolean isReady) {

                mViewHolder.getInputLayout().clearErrors();
                mViewHolder.getLoginButton().setEnabled(isReady);

            }

            @Override
            public void onInfoClicked(final ImageView view) {
                // TODO open help activity instead
                mCallbacks.showLoginHelp(view);
            }
        });

        mViewHolder.mSteamButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(final View view) {
                                                            mCallbacks.showSteamLogin(view);
                                                        }
                                                    }
        );

    }

    protected void login(final String username) {
        if (TextUtils.isNotEmpty(username)) {
            AppLog.d("Sign in for username " + username);
            mCallbacks.login(mViewHolder.getInputLayout().getUsername());
        } else {
            AppLog.e("Null username!!!!");
        }
    }

    void showLogin() {
        mViewHolder.getViewFlipper().setDisplayedChild(INDEX_LOGIN);
    }

    void showProgress() {
        mViewHolder.getInputLayout().clearErrors();
        mViewHolder.getViewFlipper().setDisplayedChild(INDEX_PROGRESS);
    }

    public void setError(final UiDataLoadError error) {
        this.setError(error.getMessage());
    }


    public void setError(final CharSequence message) {
        mViewHolder.getInputLayout().setError(message);
    }


}
