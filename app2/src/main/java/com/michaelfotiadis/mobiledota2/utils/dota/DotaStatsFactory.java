package com.michaelfotiadis.mobiledota2.utils.dota;

import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DotaStatsFactory {

    private final long mAccountId3;
    private final List<MatchDetails> mMatches;
    private final List<HeroPatchAttributes> mAttributesList;
    private final Map<String, HeroPatchAttributes> mAttributesMap;

    public DotaStatsFactory(final long accountId3,
                            final List<MatchDetails> matches,
                            final List<HeroPatchAttributes> attributesList) {
        mAccountId3 = accountId3;
        mMatches = matches;
        mAttributesList = attributesList;
        mAttributesMap = new HashMap<>();
    }

    public HeroStatistics getStatsForHero(final Hero hero) {

        int timesPlayed = 0;
        int timesWon = 0;
        int totalKills = 0;
        int totalAssists = 0;
        int totalDeaths = 0;
        int totalGpM = 0;
        int totalXpM = 0;
        int totalLastHits = 0;
        int totalDenies = 0;
        int primaryStat = -1;

        final HeroStatistics.Builder builder = HeroStatistics.newBuilder();

        final HeroPatchAttributes bestMatchDetails;
        if (mAttributesMap.containsKey(hero.getName())) {
            bestMatchDetails = mAttributesMap.get(hero.getName());
        } else {
            bestMatchDetails = DotaGeneralUtils.getClosestMatchingDetails(hero, mAttributesList);

            if (bestMatchDetails != null) {
                mAttributesMap.put(hero.getName(), bestMatchDetails);
            }
        }


        for (final MatchDetails match : mMatches) {

            // set the match first or the utilities will not work
            final DotaMatchHelper matchHelper = new DotaMatchHelper(mAccountId3, match);


            if (bestMatchDetails != null) {
                primaryStat = bestMatchDetails.getAttribute();
            }

            final PlayerDetails player = matchHelper.getPlayer();
            if (player != null && hero.getId().equals(player.getHeroId())) {
                timesPlayed++;
                if (matchHelper.isPlayerVictorious()) {
                    timesWon++;
                }
                totalKills += player.getKills();
                totalAssists += player.getAssists();
                totalDeaths += player.getDeaths();
                totalGpM += player.getGoldPerMin();
                totalXpM += player.getXpPerMin();
                totalLastHits += player.getLastHits();
                totalDenies += player.getDenies();
            }
        }
        builder.withHeroId(hero.getId())
                .withHeroName(hero.getName())
                .withLocalisedName(hero.getLocalizedName())
                .withPrimaryStat(primaryStat)
                .withTimesPlayed(timesPlayed)
                .withTimesWon(timesWon)
                .withTotalKills(totalKills)
                .withTotalAssists(totalAssists)
                .withTotalDeaths(totalDeaths)
                .withTotalGpM(totalGpM)
                .withTotalXpM(totalXpM)
                .withTotalLastHits(totalLastHits)
                .withTotalDenies(totalDenies);


        return builder.build();
    }


}
