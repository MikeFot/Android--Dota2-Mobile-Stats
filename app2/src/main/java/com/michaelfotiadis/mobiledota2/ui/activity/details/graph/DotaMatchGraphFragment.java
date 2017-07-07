package com.michaelfotiadis.mobiledota2.ui.activity.details.graph;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.details.BaseDetailsFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.details.MatchContainer;
import com.michaelfotiadis.mobiledota2.ui.activity.details.graph.recycler.MultipleGraphsRecyclerAdapter;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.recycler.MatchesItemAnimator;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.mobiledota2.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.mobiledota2.utils.dota.DotaMatchHelper;

import java.util.Arrays;

import butterknife.BindView;

public class DotaMatchGraphFragment extends BaseDetailsFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    public static Fragment newInstance(final MatchContainer match) {
        final DotaMatchGraphFragment fragment = new DotaMatchGraphFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA, match);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_default_recycler;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);

        mRecyclerView.setHasFixedSize(true);

        final int columns = RecyclerUtils.getColumnsForScreen(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        final DotaMatchHelper helper = new DotaMatchHelper(getMatch());
        final MultipleGraphsRecyclerAdapter adapter = new MultipleGraphsRecyclerAdapter(getActivity(), getMatch(), helper.getPlayer());

        final RecyclerManager<Integer> recyclerManager = new RecyclerManager.Builder<>(adapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setAnimator(new MatchesItemAnimator())
                .setEmptyMessage("Nothing to see here")
                .build();

        recyclerManager.addItems(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));

    }

}
