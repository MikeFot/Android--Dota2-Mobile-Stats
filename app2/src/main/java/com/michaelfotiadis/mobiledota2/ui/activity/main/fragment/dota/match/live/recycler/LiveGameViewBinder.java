package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.live.recycler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.dota.DotaGeneralUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.live.Faction;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveBan;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;
import com.michaelfotiadis.steam.data.dota2.model.live.LivePick;
import com.michaelfotiadis.steam.data.dota2.model.live.LivePlayerDetails;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveTeam;
import com.michaelfotiadis.steam.data.dota2.model.live.Scoreboard;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LiveGameViewBinder extends BaseRecyclerViewBinder<LiveGameViewHolder, LiveGame> {

    private final ImageLoader mImageLoader;
    private final List<Hero> mHeroes;
    private final List<League> mLeagues;
    private final OnItemSelectedListener<LiveGame> mListener;

    LiveGameViewBinder(final Context context,
                       final ImageLoader imageLoader,
                       final List<Hero> heroes,
                       final List<League> leagues,
                       final OnItemSelectedListener<LiveGame> listener) {
        super(context);
        mImageLoader = imageLoader;
        mHeroes = heroes;
        mLeagues = leagues;
        mListener = listener;
    }

    @Override
    protected void reset(final LiveGameViewHolder holder) {

    }

    @Override
    protected void setData(final LiveGameViewHolder holder, final LiveGame item) {

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

        final LiveFactionViewHolder radiantHolder = holder.mRadiantViewHolder;
        final LiveFactionViewHolder direHolder = holder.mDireViewHolder;

        final String leagueTitle = DotaGeneralUtils.getLeagueTitle(item.getLeagueId(), mLeagues);
        if (leagueTitle != null) {
            holder.mLeagueTitle.setText(leagueTitle);
        } else {
            showView(holder.mLeagueTitle, false);
        }

        holder.mMatchId.setText(String.valueOf(item.getMatchId()));

        radiantHolder.mFactionTitle.setText(R.string.faction_radiant);
        radiantHolder.mFactionTitle.setTextColor(getColor(R.color.map_green_radiant_dark));

        direHolder.mFactionTitle.setText(R.string.faction_dire);
        direHolder.mFactionTitle.setTextColor(getColor(R.color.map_red_dire_dark));

        final Scoreboard scoreboard = item.getScoreboard();

        if (scoreboard != null) {

            if (scoreboard.getDuration() != null) {
                holder.mDuration.setText(getDuration(scoreboard.getDuration()));
            } else {
                showView(holder.mLayoutDuration, false);
            }

            if (scoreboard.getRadiant() != null) {
                final Faction radiant = scoreboard.getRadiant();
                setUpNetWorth(radiantHolder.mNetWorth, radiant.getPlayers());
                final String radiantScore = String.valueOf(radiant.getScore());
                holder.mRadiantScore.setText(radiantScore);
                if (radiant.getPicks() != null) {
                    setUpPicks(holder.mRadiantViewHolder.mPicksHolder, radiant.getPicks());
                }
                if (radiant.getBans() != null) {
                    setUpBans(holder.mRadiantViewHolder.mBansHolder, radiant.getBans());
                }


            }

            if (scoreboard.getDire() != null) {
                final Faction dire = scoreboard.getDire();
                final String direScore = String.valueOf(dire.getScore());
                holder.mDireScore.setText(direScore);
                setUpNetWorth(direHolder.mNetWorth, dire.getPlayers());
                if (dire.getPicks() != null) {
                    setUpPicks(holder.mDireViewHolder.mPicksHolder, dire.getPicks());
                }
                if (dire.getBans() != null) {
                    setUpBans(holder.mDireViewHolder.mBansHolder, dire.getBans());
                }

            }
        }

        setUpTeamName(radiantHolder.mTeamTitle, item.getRadiantTeam(), item.getRadiantSeriesWins());
        setUpTeamName(direHolder.mTeamTitle, item.getDireTeam(), item.getDireSeriesWins());

        if (item.getSpectators() != null) {
            //noinspection RedundantStringFormatCall
            holder.mSpectators.setText(String.format(Locale.US, item.getSpectators().toString()));
        } else {
            holder.mSpectators.setText("0");
        }

        final String radiantName = item.getRadiantTeam() != null ? item.getRadiantTeam().getTeamName() : "";
        final String direName = item.getDireTeam() != null ? item.getDireTeam().getTeamName() : "";
        AppLog.d("Binding item: '" + radiantName + "' VS '" + direName + "'");

    }

    private void setUpNetWorth(final TextView textView, final List<LivePlayerDetails> players) {
        if (players != null) {
            showView(textView, true);
            textView.setText(getContext().getString(R.string.net_worth, getNetWorth(players)));
        } else {
            showView(textView, false);
        }
    }

    private static int getNetWorth(final List<LivePlayerDetails> players) {

        int worth = 0;


        for (final LivePlayerDetails details : players) {
            if (details != null && details.getNetWorth() != null) {
                worth += details.getNetWorth();
            }
        }

        return worth;


    }

    private void setUpPicks(final LiveFiveHeroViewHolder holder, final List<LivePick> picks) {
        if (picks.size() >= 1) {
            showHeroImage(picks.get(0).getHeroId(), holder.mImageHero1);
        }
        if (picks.size() >= 2) {
            showHeroImage(picks.get(1).getHeroId(), holder.mImageHero2);
        }
        if (picks.size() >= 3) {
            showHeroImage(picks.get(2).getHeroId(), holder.mImageHero3);
        }
        if (picks.size() >= 4) {
            showHeroImage(picks.get(3).getHeroId(), holder.mImageHero4);
        }
        if (picks.size() >= 5) {
            showHeroImage(picks.get(4).getHeroId(), holder.mImageHero5);
        }
    }

    private void setUpBans(final LiveFiveHeroViewHolder holder, final List<LiveBan> bans) {
        if (bans.size() >= 1) {
            showHeroImage(bans.get(0).getHeroId(), holder.mImageHero1);
        }
        if (bans.size() >= 2) {
            showHeroImage(bans.get(1).getHeroId(), holder.mImageHero2);
        }
        if (bans.size() >= 3) {
            showHeroImage(bans.get(2).getHeroId(), holder.mImageHero3);
        }
        if (bans.size() >= 4) {
            showHeroImage(bans.get(3).getHeroId(), holder.mImageHero4);
        }
        if (bans.size() >= 5) {
            showHeroImage(bans.get(4).getHeroId(), holder.mImageHero5);
        }
    }

    private void showHeroImage(final Integer heroId, final ImageView imageView) {

        final Hero hero = DotaGeneralUtils.getHeroForId(heroId, mHeroes);
        if (hero != null) {
            mImageLoader.loadHero(imageView, hero.getName());
        } else {
            showView(imageView, false);
        }

    }

    private String getDuration(final Double duration) {

        if (duration == null) {
            return "Picking phase";
        }

        final long minutes = TimeUnit.SECONDS.toMinutes(duration.longValue());

        final String message;
        if (minutes == 0) {
            message = "Starting";
        } else if (minutes == 1) {
            message = "1 minute";
        } else {
            message = minutes + " minutes";
        }

        return message;
    }

    private void setUpTeamName(final TextView target,
                               final LiveTeam liveTeam,
                               final Integer seriesWins) {

        if (liveTeam == null) {
            showView(target, false);
        } else {
            showView(target, true);
            String message = liveTeam.getTeamName();

            if (seriesWins != null) {
                message += seriesWins == 1 ? " (1 series win)" : String.format(Locale.US, " (%d series wins)", seriesWins);
            }
            target.setText(message);

        }

    }

}
