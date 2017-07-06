package com.michaelfotiadis.dota2viewer.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;

@SuppressLint("InflateParams")
public class AboutDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View content = inflater.inflate(R.layout.dialog_about, null, false);

        String version;
        try {
            final PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "";
        }
        final TextView versionView = (TextView) content.findViewById(R.id.version);
        versionView.setText(version);


        //noinspection AnonymousInnerClassMayBeStatic
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setView(content)
                .setPositiveButton(R.string.label_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }


}