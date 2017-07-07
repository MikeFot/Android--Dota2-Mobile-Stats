package com.michaelfotiadis.mobiledota2.ui.activity.details;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.mobiledota2.ui.core.base.fragment.BaseFragment;

import butterknife.ButterKnife;

public abstract class BaseDetailsFragment extends BaseFragment {

    protected static final String EXTRA = "extra";

    protected MatchContainer mMatch;


    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMatch = getArguments().getParcelable(EXTRA);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    protected MatchContainer getMatch() {
        return mMatch;
    }

}
