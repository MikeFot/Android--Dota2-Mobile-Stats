package com.michaelfotiadis.dota2viewer.ui.activity.details.match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.details.BaseDetailsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.ui.activity.details.match.view.DotaMatchDetailsViewBinder;
import com.michaelfotiadis.dota2viewer.ui.activity.details.match.view.FactionViewHolder;
import com.michaelfotiadis.dota2viewer.ui.activity.details.match.view.MatchDetailsViewHolder;
import com.michaelfotiadis.dota2viewer.ui.activity.details.match.view.MatchStatsRowViewHolder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaMatchHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DotaMatchDetailsFragment extends BaseDetailsFragment {

    @Inject
    ImageLoader mImageLoader;

    // view holders
    private MatchDetailsViewHolder mParentViewHolder;
    private FactionViewHolder mRadiantFactionViewHolder;
    private FactionViewHolder mDireFactionViewHolder;
    private List<MatchStatsRowViewHolder> mListRadiantStatsHolder;
    private List<MatchStatsRowViewHolder> mListDireStatsHolder;

    public static Fragment newInstance(final MatchContainer match) {
        final DotaMatchDetailsFragment fragment = new DotaMatchDetailsFragment();
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA, match);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_match_details;
    }

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // start with the parent layout
        mParentViewHolder = new MatchDetailsViewHolder(view);
        mParentViewHolder.layoutRadiantFaction.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_green_100));
        mParentViewHolder.layoutDireFaction.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_red_100));

        // do the faction layouts next
        mRadiantFactionViewHolder = new FactionViewHolder(mParentViewHolder.layoutRadiantFaction);
        mDireFactionViewHolder = new FactionViewHolder(mParentViewHolder.layoutDireFaction);

        // do the rows for radiant
        mListRadiantStatsHolder = new ArrayList<>();
        mListRadiantStatsHolder.add(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutRow1));
        mListRadiantStatsHolder.add(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutRow2));
        mListRadiantStatsHolder.add(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutRow3));
        mListRadiantStatsHolder.add(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutRow4));
        mListRadiantStatsHolder.add(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutRow5));
        mListDireStatsHolder = new ArrayList<>();
        mListDireStatsHolder.add(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutRow1));
        mListDireStatsHolder.add(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutRow2));
        mListDireStatsHolder.add(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutRow3));
        mListDireStatsHolder.add(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutRow4));
        mListDireStatsHolder.add(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutRow5));

        populateFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void populateFragment() {
        if (getCurrentUserId3() == null) {
            AppLog.e("Null account id. Abandoning fragment population.");
            return;
        }
        // initialise the dota match utils

        final DotaMatchHelper helper = new DotaMatchHelper(getCurrentUserId3(), mMatch.getMatchDetails());
        // we need the extra dota context utils here
        final DotaMatchDetailsViewBinder binder = new DotaMatchDetailsViewBinder(getActivity(), mImageLoader, helper, mMatch.getHeroes(), mMatch.getGameItems());

        // set up the header textViewHeroName view
        binder.setUpMatchDetailsHeader(mParentViewHolder.textHeaderVictory);

        int position = 0;
        // populate the radiant part
        for (final MatchStatsRowViewHolder row : mListRadiantStatsHolder) {
            binder.setUpPlayerStatsRowViewHolder(row, position);
            position++;
        }
        // now do the dire part retaining the value of the integer
        for (final MatchStatsRowViewHolder row : mListDireStatsHolder) {
            binder.setUpPlayerStatsRowViewHolder(row, position);
            position++;
        }
        binder.setUpMatchDetailsFooter(new MatchStatsRowViewHolder(mRadiantFactionViewHolder.layoutFooter), true);
        binder.setUpMatchDetailsFooter(new MatchStatsRowViewHolder(mDireFactionViewHolder.layoutFooter), false);

    }


}
