package com.michaelfotiadis.mobiledota2.utils.dota;

import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.mobiledota2.data.persistence.model.HeroStatistics;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.steam.data.dota2.model.hero.Hero;
import com.michaelfotiadis.steam.data.dota2.model.hero.HeroDetails;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;
import com.michaelfotiadis.steam.data.dota2.model.leagues.League;
import com.michaelfotiadis.steam.data.dota2.model.live.LiveGame;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SearchFilterUtils {

    private SearchFilterUtils() {
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

                final String leagueName = DotaGeneralUtils.getLeagueTitle(game.getLeagueId(), leagues);
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

    public static List<GameItem> getFilteredGameItems(final List<GameItem> data,
                                                      final String query) {

        if (TextUtils.isEmpty(query)) {
            return data;
        } else {
            final List<GameItem> filteredList = new ArrayList<>();
            for (final GameItem item : data) {
                if (item.getLocalizedName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            return filteredList;
        }

    }
}
