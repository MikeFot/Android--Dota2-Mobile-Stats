package com.michaelfotiadis.dota2viewer.ui.activity.details.graph;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.details.BaseDetailsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.activity.details.graph.recycler.MultipleStatViewsRecyclerAdapter;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler.MatchesItemAnimator;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager.RecyclerManager;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.SimpleUiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;
import com.michaelfotiadis.dota2viewer.ui.view.utils.RecyclerUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaMatchHelper;

import butterknife.BindView;


public class DotaMatchPlayerFragment extends BaseDetailsFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Fragment newInstance(final MatchContainer match) {
        final DotaMatchPlayerFragment fragment = new DotaMatchPlayerFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA, match);
        fragment.setArguments(args);
        return fragment;
    }

    public DotaMatchPlayerFragment() {
        // Required empty public constructor
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
        final MultipleStatViewsRecyclerAdapter adapter = new MultipleStatViewsRecyclerAdapter(getActivity(), getMatch(), helper.getPlayer());

        final RecyclerManager<Integer> recyclerManager = new RecyclerManager.Builder<>(adapter)
                .setRecycler(mRecyclerView)
                .setStateKeeper(uiStateKeeper)
                .setAnimator(new MatchesItemAnimator())
                .setEmptyMessage("Nothing to see here")
                .build();

        recyclerManager.addItem(0);
        recyclerManager.addItem(1);
//        setUpHeader();
    }



 /*   class HeaderViewHolder {
        @InjectView(R.id.image_view_hero)
        ImageView imageViewHero;
        @InjectView(R.id.text_view_hero_name)
        TextView textViewHeroName;
        @InjectView(R.id.text_content_faction)
        TextView textViewFactionName;
        @InjectView(R.id.text_content_result)
        TextView textViewResult;

        public HeaderViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }*/
}
