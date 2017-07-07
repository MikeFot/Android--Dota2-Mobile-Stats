package com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.model.GameDateStats;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinsDialog extends DialogFragment {

    private static final String DIALOG_EXTRAS = "extras";

    private GameDateStats mStats;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static DialogFragment newInstance(final GameDateStats stats) {

        final DialogFragment dialogFragment = new WinsDialog();
        final Bundle args = new Bundle();
        args.putParcelable(DIALOG_EXTRAS, stats);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStats = getArguments().getParcelable(DIALOG_EXTRAS);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Calendar Wins Dialog")
                .putContentType("Dialog"));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String message = getString(R.string.message_wins, mStats.getWins(), mStats.getLosses());
        builder.setTitle(message);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_wins, null);
        ButterKnife.bind(this, view);


        mRecyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final WinsRecyclerAdapter winsRecyclerAdapter = new WinsRecyclerAdapter(getContext());
        mRecyclerView.setAdapter(winsRecyclerAdapter);

        winsRecyclerAdapter.setItems(mStats.getSequence());

        builder.setView(view);
        builder.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }


    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
