package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.recycler.PlayerRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler.MatchesItemAnimator;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseRecyclerFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseUserRecyclerFragment extends BaseRecyclerFragment<PlayerWrapper> implements OnUserSelectedListener {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerManager<PlayerWrapper> mRecyclerManager;
    protected PlayerRecyclerAdapter mAdapter;

    @Inject
    ImageLoader mImageLoader;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_default_recycler, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initRecyclerManager(final View view) {
        final UiStateKeeper uiStateKeeper = new SimpleUiStateKeeper(view, mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PlayerRecyclerAdapter(getActivity(), mImageLoader, this);
        mRecyclerManager = new RecyclerManager.Builder<>(mAdapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setAnimator(new MatchesItemAnimator())
                .setEmptyMessage("Nothing to see here")
                .build();

        if (TextUtils.isEmpty(getCurrentUserId())) {
            showNoId();
        } else {
            mRecyclerManager.updateUiState();
        }
    }

}
