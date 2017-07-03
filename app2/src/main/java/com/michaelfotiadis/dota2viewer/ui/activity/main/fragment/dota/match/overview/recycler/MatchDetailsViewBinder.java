package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.recycler;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchDetailsItem;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.model.MatchItem;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.ui.view.ThreeBarView;
import com.michaelfotiadis.dota2viewer.ui.view.utils.SimpleSpanBuilder;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaMatchHelper;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaResourceUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.DotaTimeUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.provider.image.Size;

public class MatchDetailsViewBinder extends BaseRecyclerViewBinder<MatchDetailsViewHolder, MatchDetailsItem> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<MatchItem> mListener;

    protected MatchDetailsViewBinder(final Context context,
                                     final ImageLoader imageLoader,
                                     final OnItemSelectedListener<MatchItem> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final MatchDetailsViewHolder holder) {

        holder.getRoot().setOnClickListener(null);

    }

    @Override
    protected void setData(final MatchDetailsViewHolder holder, final MatchDetailsItem item) {


        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

        final long playerId = item.getPlayerId();
        final MatchDetails details = item.getMatchDetails();

        final DotaMatchHelper helper = new DotaMatchHelper(playerId, details);

        final String timeAgo = DotaTimeUtils.getTimeAgoForMatch(
                getContext().getResources(),
                details.getStartTime(),
                details.getDuration());

        holder.mTextContentHoursAgo.setText(timeAgo);

        holder.mTextContentMatchDuration.setText(String.format("%s %s", getString(R.string.duration), helper.getDurationForMatch()));

        setupKdaText(holder, helper);

        holder.mTextContentGameMode.setText(DotaResourceUtils.getDescriptionForGameMode(details.getGameModeAsEnum()));
        holder.mTextContentLobbyType.setText(DotaResourceUtils.getDescriptionForLobbyType(details.getLobbyTypeAsEnum()));

        setUpThreeBarView(helper, holder.mBarThreeColor, holder.mColorKills, holder.mColorDeaths, holder.mColorAssists);


        showView(holder.mTextLeaver, false);
        showView(holder.mTextLastHits, helper.getPlayer() != null);

        if (helper.getPlayer() != null) {

            final int leaverRes = DotaGeneralUtils.getLeaverStatus(helper.getPlayer().getLeaverStatusAsEnum());
            if (leaverRes != 0) {
                showView(holder.mTextLeaver, true);
                holder.mTextLeaver.setText(getString(leaverRes));
            }

            final int lastHits = helper.getPlayer().getLastHits();
            final int denies = helper.getPlayer().getDenies();
            holder.mTextLastHits.setText(getContext().getString(R.string.placeholder_last_hits, lastHits, denies));

            for (final Hero hero : item.getHeroes()) {
                if (hero.getId().equals(helper.getPlayer().getHeroId())) {
                    mImageLoader.loadHero(holder.mImageHero, hero.getName(), Size.FULL_VERTICAL);
                    break;
                }
            }

        }

        setUpViewByVictory(holder.mTextContentVictory, helper.isPlayerVictorious());


    }

    private void setupKdaText(final MatchDetailsViewHolder holder, final DotaMatchHelper helper) {
        final SimpleSpanBuilder sb = new SimpleSpanBuilder()
                .append(getString(R.string.prefix_kda))
                .append(String.valueOf(helper.getPlayerKills()), new ForegroundColorSpan(holder.mColorKills))
                .append("/")
                .append(String.valueOf(helper.getPlayerDeaths()), new ForegroundColorSpan(holder.mColorDeaths))
                .append("/")
                .append(String.valueOf(helper.getPlayerAssists()), new ForegroundColorSpan(holder.mColorAssists));


        holder.mTextContentKda.setText(sb.build());
    }


    private static void setUpThreeBarView(@NonNull final DotaMatchHelper helper,
                                          @NonNull final ThreeBarView bar,
                                          @ColorRes final int colorKills,
                                          @ColorRes final int colorDeaths,
                                          @ColorRes final int colorAssists) {
        bar.setValueOne(helper.getPlayerKills());
        bar.setValueTwo(helper.getPlayerDeaths());
        bar.setValueThree(helper.getPlayerAssists());
        bar.setColorOne(colorKills);
        bar.setColorTwo(colorDeaths);
        bar.setColorThree(colorAssists);

        bar.invalidate();
    }

    private void setUpViewByVictory(final TextView textView,
                                    final boolean isVictorious) {
        @StringRes final int contentId;
        final int color;

        if (isVictorious) {
            contentId = R.string.label_player_won;
            color = getColor(R.color.player_won);
        } else {
            contentId = R.string.label_player_lost;
            color = getColor(R.color.player_lost);
        }
        textView.setText(contentId);
        textView.setTextColor(color);
    }


}
