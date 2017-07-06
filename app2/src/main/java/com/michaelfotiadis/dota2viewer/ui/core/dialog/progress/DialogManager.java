package com.michaelfotiadis.dota2viewer.ui.core.dialog.progress;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.dialog.alert.AlertDialogFactory;


public class DialogManager {

    private static final String DIALOG_TAG = DialogManager.class.getSimpleName() + ".progress_tag";
    private AlertDialog mErrorDialog;


    private final FragmentManager mFragmentManager;
    private final Resources mResources;
    private final AlertDialogFactory mFactory;

    public DialogManager(final FragmentManager fragmentManager,
                         final Resources resources,
                         final AlertDialogFactory factory) {
        mFragmentManager = fragmentManager;
        mResources = resources;
        mFactory = factory;
    }

    public void showProgress() {
        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
        }

        ProgressDialogFragment fragment = (ProgressDialogFragment) mFragmentManager.findFragmentByTag(DIALOG_TAG);
        if (fragment == null) {
            fragment = new ProgressDialogFragment();
            fragment.setCancelable(false);
            mFragmentManager.beginTransaction()
                    .add(fragment, DIALOG_TAG)
                    .commitAllowingStateLoss();
        }
    }

    public void dismissProgress() {
        final ProgressDialogFragment fragment = (ProgressDialogFragment) mFragmentManager.findFragmentByTag(DIALOG_TAG);
        if (fragment != null) {
            // fragment.dismissAllowingStateLoss();
            mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }


    public void showErrorDialog(final CharSequence message) {

        mErrorDialog = mFactory.create(
                "",
                message,
                mResources.getString(R.string.message_ok));
        mErrorDialog.show();
    }

    public void showErrorDialog(final CharSequence message,
                                final CharSequence positiveText,
                                final DialogInterface.OnClickListener positiveListener,
                                final CharSequence negativeText,
                                final DialogInterface.OnClickListener negativeListener) {

        mErrorDialog = mFactory.create(
                "",
                message,
                positiveText,
                positiveListener,
                negativeText,
                negativeListener);
        mErrorDialog.show();
    }

}
