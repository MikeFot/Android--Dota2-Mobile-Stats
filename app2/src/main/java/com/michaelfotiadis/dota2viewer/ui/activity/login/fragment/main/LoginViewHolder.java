package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.main;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.view.holder.BaseViewHolder;
import com.michaelfotiadis.dota2viewer.ui.view.credentials.CredentialsInputLayout;

import butterknife.BindView;

public class LoginViewHolder extends BaseViewHolder {

    @BindView(R.id.view_flipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.input_layout)
    CredentialsInputLayout mInputLayout;
    @BindView(R.id.button_login)
    Button mLoginButton;


    protected LoginViewHolder(final View view) {
        super(view);

        getViewFlipper().setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right_chrome));
        getViewFlipper().setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left_chrome));

    }

    public ViewFlipper getViewFlipper() {
        return mViewFlipper;
    }

    public CredentialsInputLayout getInputLayout() {
        return mInputLayout;
    }

    public Button getLoginButton() {
        return mLoginButton;
    }

}
