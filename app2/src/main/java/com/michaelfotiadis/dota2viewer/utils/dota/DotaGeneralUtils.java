package com.michaelfotiadis.dota2viewer.utils.dota;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.Pair;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.model.GameDateStats;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.AbilityUpgrade;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;
import com.michaelfotiadis.steam.data.dota2.types.LeaverStatus;
import com.michaelfotiadis.steam.data.dota2.types.StatType;
import com.michaelfotiadis.steam.data.steam.player.library.Game;
import com.michaelfotiadis.validator.annotated.validators.text.TextNumericValidatorHelper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 */
public final class DotaGeneralUtils {


    private DotaGeneralUtils() {
    }


    public static List<GameDateStats> getGameDateStatsFromMatchHistory(final long accountId,
                                                                       final List<MatchDetails> history) {
        final List<GameDateStats> stats = new ArrayList<>();

        final HashSet<Pair<String, String>> dateSet = new HashSet<>();
        for (final MatchDetails match : history) {
            final DateTime dateTime = new DateTime((match.getStartTime() + match.getDuration()) * 1000);
            dateSet.add(new Pair<>(dateTime.dayOfYear().getAsString(), dateTime.year().getAsString()));
        }

        for (final Pair<String, String> item : dateSet) {
            final int dayOfYear = Integer.valueOf(item.first);
            final int year = Integer.valueOf(item.second);
            int wins = 0;
            int losses = 0;
            final List<Boolean> sequence = new ArrayList<>();
            for (final MatchDetails match : history) {
                final DotaMatchHelper dotaMatchUtils = new DotaMatchHelper(accountId, match);
                final DateTime dateTime = new DateTime((match.getStartTime() + match.getDuration()) * 1000, DateTimeZone.UTC);
                if (dateTime.dayOfYear().getAsString().equals(item.first) &&
                        dateTime.year().getAsString().equals(item.second)) {

                    final boolean isWin = dotaMatchUtils.isPlayerVictorious();

                    sequence.add(isWin);
                    if (isWin) {
                        wins += 1;
                    } else {
                        losses += 1;
                    }
                }
            }
            stats.add(new GameDateStats(dayOfYear, year, wins, losses, sequence));
        }

        return stats;
    }


    public static int calculateSumValue(final MatchDetails match,
                                        final boolean isRadiant,
                                        final StatType statType) {
        int startingIndex = 0;
        if (!isRadiant) {
            startingIndex = 5;
        }

        int sum = 0;

        int i = startingIndex;
        while (i < startingIndex + 5) {
            sum += getValueForStatType(match.getPlayers().get(i), statType);
            i++;
        }
        return sum;
    }

    public static int getValueForStatType(final PlayerDetails player, final StatType statType) {

        switch (statType) {


            case LEVEL:
                return player.getLevel();
            case KILLS:
                return player.getKills();
            case DEATHS:
                return player.getDeaths();
            case ASSISTS:
                return player.getAssists();
            case GOLD:
                return player.getGold();
            case GOLD_SPENT:
                return player.getGoldSpent();
            case LAST_HITS:
                return player.getLastHits();
            case DENIES:
                return player.getDenies();
            case XP_PER_MIN:
                return player.getXpPerMin();
            case GOLD_PER_MIN:
                return player.getGoldPerMin();
            case HERO_DAMAGE:
                return player.getHeroDamage();
            case HERO_HEALING:
                return player.getHeroHealing();
            case TOWER_DAMAGE:
                return player.getTowerDamage();
            default:
                return 0;
        }

    }

    public static int calculateAverageValue(final MatchDetails match,
                                            final boolean isRadiant,
                                            final StatType statType) {
        int startingIndex = 0;
        if (!isRadiant) {
            startingIndex = 128;
        }

        int sum = 0;

        int i = startingIndex;
        while (i < startingIndex + 5) {
            sum += getValueForStatType(match.getPlayers().get(i), statType);
            i++;
        }
        return sum / 5;
    }

    public static List<AbilityUpgrade> getLevelProgression(final MatchDetails match,
                                                           final boolean isRadiant) {
        final List<AbilityUpgrade> abilityUpgrades = new ArrayList<>();

        for (final PlayerDetails player : match.getPlayers()) {
            if (isRadiant && 5 > player.getPlayerSlot()) {
                // add the Radiant upgrades
                for (final AbilityUpgrade upgrade : player.getAbilityUpgrades()) {
                    abilityUpgrades.add(upgrade);
                }
            }
            if (!isRadiant && 128 <= player.getPlayerSlot()) {
                // add the Dire upgrades
                for (final AbilityUpgrade upgrade : player.getAbilityUpgrades()) {
                    abilityUpgrades.add(upgrade);
                }
            }
        }
        // we require an ordered list for this
        Collections.sort(abilityUpgrades, new Comparator<AbilityUpgrade>() {
            @Override
            public int compare(final AbilityUpgrade upg1, final AbilityUpgrade upg2) {
                return upg1.getTime() - upg2.getTime();
            }
        });
        return abilityUpgrades;
    }

    public static List<League> getFilteredLeagues(final List<League> data,
                                                  final String query) {

        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<League> filteredList = new ArrayList<>();
            for (final League league : data) {

                if (TextUtils.isNotEmpty(league.getName())
                        && league.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(league);
                } else if (TextUtils.isNotEmpty(league.getDescription())
                        && league.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(league);
                } else if (TextNumericValidatorHelper.isNumeric(query) && league.getLeagueid().toString().equals(query)) {
                    filteredList.add(league);
                }
            }
            return filteredList;
        }
    }

    public static List<HeroStatistics> getFilteredHeroStatistics(final List<HeroStatistics> data,
                                                                 final String query) {

        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<HeroStatistics> filteredList = new ArrayList<>();
            for (final HeroStatistics hero : data) {
                if (hero.getLocalisedName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(hero);
                }
            }
            return filteredList;
        }

    }

    @Nullable
    public static String getLeagueTitle(final Long leagueId, final List<League> leagues) {
        String leagueTitle = null;
        if (leagueId != null) {
            for (final League league : leagues) {
                if (leagueId.equals(league.getLeagueid().longValue())) {
                    leagueTitle = league.getName();
                    break;
                }
            }
        }
        return leagueTitle;
    }

    public static Collection<LiveGame> getFilteredLiveGames(final List<LiveGame> games,
                                                            final List<League> leagues,
                                                            final List<Hero> heroes,
                                                            final String query) {
        if (TextUtils.isEmpty(query)) {
            return games;
        } else {
            final Set<LiveGame> filteredList = new HashSet<>();
            for (final LiveGame game : games) {

                final String leagueName = getLeagueTitle(game.getLeagueId(), leagues);
                if (game.getLeagueId() != null &&
                        game.getLeagueId().toString().toLowerCase().contains(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match League ID");
                    filteredList.add(game);
                } else if (game.getMatchId() != null
                        && game.getMatchId().toString().contains(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match Match ID");
                    filteredList.add(game);
                } else if (game.getLeagueGameId() != null
                        && game.getLeagueGameId().toString().equals(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match League Game ID");
                    filteredList.add(game);
                } else if (leagueName != null && leagueName.toLowerCase().contains(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match League Name: " + leagueName);
                    filteredList.add(game);
                } else if (game.getRadiantTeam() != null
                        && game.getRadiantTeam().getTeamName() != null
                        && game.getRadiantTeam().getTeamName().toLowerCase().contains(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match Radiant: " + game.getRadiantTeam().getTeamName());
                    filteredList.add(game);
                } else if (game.getDireTeam() != null
                        && game.getDireTeam().getTeamName() != null
                        && game.getDireTeam().getTeamName().toLowerCase().contains(query.toLowerCase())) {
                    AppLog.d(game.getMatchId() + " Match Dire: " + game.getDireTeam().getTeamName());
                    filteredList.add(game);
                }

            }
            return filteredList;
        }
    }

    public static List<Game> getFilteredLibrary(final List<Game> data, final String query) {

        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<Game> filteredList = new ArrayList<>();
            for (final Game game : data) {
                if (game.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(game);
                } else if (game.getAppId().toString().contains(query)) {
                    filteredList.add(game);
                }
            }
            return filteredList;
        }

    }

    public static List<HeroDetails> getFilteredHeroDetailsList(final List<HeroDetails> data,
                                                               final String query) {
        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<HeroDetails> filteredList = new ArrayList<>();
            for (final HeroDetails hero : data) {
                if (hero.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(hero);
                }
            }
            return filteredList;
        }
    }

    public static List<Hero> getFilteredHeroList(final List<Hero> data,
                                                 final String query) {
        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<Hero> filteredList = new ArrayList<>();
            for (final Hero hero : data) {
                if (hero.getName().toLowerCase().contains(query.toLowerCase())
                        || hero.getLocalizedName().toLowerCase().contains(query.toLowerCase())
                        || hero.getId().toString().equals(query.toLowerCase())) {
                    filteredList.add(hero);
                }
            }
            return filteredList;
        }
    }

    public static List<HeroPatchAttributes> getFilteredHeroPatchList(final List<HeroPatchAttributes> data,
                                                                     final String query) {
        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<HeroPatchAttributes> filteredList = new ArrayList<>();
            for (final HeroPatchAttributes hero : data) {
                if (hero.getName().toLowerCase().contains(query.toLowerCase())
                        || hero.getHero().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(hero);
                }
            }
            return filteredList;
        }
    }

    public static List<MatchDetails> getFilteredMatchList(final long accountId,
                                                          final List<MatchDetails> matchList,
                                                          final List<Hero> heroes,
                                                          final String query) {

        if (TextUtils.isEmpty(query)) {
            return matchList;
        } else {
            final List<MatchDetails> filteredList = new ArrayList<>();


            for (final MatchDetails match : matchList) {
                final DotaMatchHelper dotaMatchUtils = new DotaMatchHelper(accountId, match);

                final String heroName = getHeroNameSafe(dotaMatchUtils.getPlayer().getHeroId(), heroes);
                if (heroName.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(match);
                }
            }
            return filteredList;
        }
    }

    @Nullable
    public static Hero getHeroForId(final int id, final List<Hero> heroes) {
        for (final Hero hero : heroes) {
            if (hero.getId() == id) {
                return hero;
            }
        }

        return null;
    }

    public static String getHeroNameSafe(final int id, final List<Hero> heroes) {
        for (final Hero hero : heroes) {
            if (hero.getId() == id) {
                return hero.getLocalizedName();
            }
        }

        return "";


    }

    @Nullable
    public static GameItem getItemForCode(final int code, final List<GameItem> items) {
        for (final GameItem item : items) {
            if (item.getId() == code) {
                return item;
            }
        }
        return null;
    }

    @Nullable
    public static HeroPatchAttributes getClosestMatchingDetails(@NonNull final Hero hero,
                                                                @NonNull final List<HeroPatchAttributes> detailsList) {

        final String heroName = hero.getName().replaceAll("npc_dota_hero_", "").trim();
        final String locName = hero.getLocalizedName().trim();

        int dist1 = Integer.MAX_VALUE;
        int dist2 = Integer.MAX_VALUE;

        HeroPatchAttributes heroDetails = null;

        for (final HeroPatchAttributes details : detailsList) {
            final String detailsName = details.getHero().trim();
            if (detailsName.equalsIgnoreCase(locName)
                    || detailsName.equalsIgnoreCase(heroName)) {
                AppLog.d("Found exact match for hero : " + heroName);
                return details;
            }
        }

        for (final HeroPatchAttributes details : detailsList) {
            final String detailsName = details.getHero().trim();
            final int innerDist1 = StringUtils.getLevenshteinDistance(heroName, detailsName);
            final int innerDist2 = StringUtils.getLevenshteinDistance(locName, detailsName);

            if (innerDist1 <= 4 || innerDist2 <= 4) {
                AppLog.d("Found <4 match for hero " + locName + " equal to " + detailsName);
                return details;
            }

            if (innerDist1 < dist1) {
                dist1 = innerDist1;
                heroDetails = details;

            }
            if (innerDist2 < dist2) {
                dist2 = innerDist2;
                heroDetails = details;
            }
        }
        if (heroDetails != null) {
            AppLog.d("Best match for hero " + heroName + " is " + heroDetails.getHero() + " for dist1= " + dist1 + " and dist2= " + dist2);
        } else {
            AppLog.w("No match for hero " + heroName);
        }
        return heroDetails;
    }

    public static long getDurationSafe(final LiveGame liveGame) {

        return liveGame == null || liveGame.getScoreboard() == null || liveGame.getScoreboard().getDuration() == null ? 0 : TimeUnit.SECONDS.toMinutes(liveGame.getScoreboard().getDuration().longValue());

    }

    public static String getHeroNameFromDataName(final String dataName) {

        final String result = dataName.replaceAll("npc_dota_hero_", "").replaceAll("_", " ");
        return WordUtils.capitalizeFully(result);

    }

    @StringRes
    public static int getLeaverStatus(final LeaverStatus leaverStatus) {

        @StringRes final int result;
        switch (leaverStatus) {
            case DISCONNECTED:
                result = R.string.leaver_dc;
                break;
            case DISCONNECTED_TOO_LONG:
                result = R.string.leaver_timeout;
                break;
            case ABANDONED:
                result = R.string.leaver_abandon;
                break;
            case AFK:
                result = R.string.leaver_afk;
                break;
            case NEVER_CONNECTED:
                result = R.string.leaver_never_connected;
                break;
            case NEVER_CONNECTED_TOO_LONG:
                result = R.string.leaver_never_connected_timeout;
                break;
            default:
                result = 0;
        }
        return result;
    }

}
