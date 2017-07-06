package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.toast.AppToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseBottomNavFragment extends BaseFragment {

    @BindView(R.id.navigation)
    protected BottomNavigationView mNavigationView;


    protected static final String NESTED_FRAGMENT_TAG = "nested_fragment_tag";
    protected static final int CONTENT_ID = R.id.nested_content;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

                        final Fragment fragment = getFragmentForId(item.getItemId());
                        final boolean isFragmentEligible = fragment != null;
                        if (isFragmentEligible) {
                            replaceContentFragment(fragment);
                        } else {
                            AppToast.show(getContext(), "Not implemented button - yet!");
                        }
                        return isFragmentEligible;
                    }

                });

        if (savedInstanceState == null) {
            mNavigationView.setSelectedItemId(getFirstMenuId());
        }
    }

    protected void replaceContentFragment(final Fragment fragment) {

        final FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if (getChildFragmentManager().findFragmentByTag(NESTED_FRAGMENT_TAG) == null) {
            fragmentTransaction.add(CONTENT_ID, fragment, NESTED_FRAGMENT_TAG);
        } else {
            fragmentTransaction.replace(CONTENT_ID, fragment, NESTED_FRAGMENT_TAG);
        }

        fragmentTransaction.commit();
    }

    protected void refreshCurrentPage() {
        mNavigationView.setSelectedItemId(mNavigationView.getSelectedItemId());
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract int getFirstMenuId();

    protected abstract Fragment getFragmentForId(final int id);

}
