package com.michaelfotiadis.mobiledota2.data.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class DataPreferences extends PreferenceHandler {

    private static final String SHARED_PREFS_FILE = "data.preferences";
    private static final String KEY_DOTA_HEROES_UPDATED = "key.table.dota.heroes.updated";
    private static final String KEY_HERO_DETAILS_UPDATED = "key.table.hero.details.updated";
    private static final String KEY_DOTA_ITEMS_UPDATED = "key.table.dota.items.updated";
    private static final String KEY_DOTA_RARITIES_UPDATED = "key.table.dota.rarities.updated";
    private static final String KEY_DOTA_LEAGUES_UPDATED = "key.table.dota.leagues.updated";
    private static final String KEY_PREFIX_USER = "key.map.steamId";
    private static final String KEY_PREFIX_LIBRARY = "key.map.library";
    private static final String KEY_PREFIX_MATCH_OVERVIEW = "key.map.match.overview";

    public DataPreferences(final Context context) {
        super(context);
    }

    public long getDotaHeroesUpdated() {
        return getSharedPreferences().getLong(KEY_DOTA_HEROES_UPDATED, Long.MIN_VALUE);
    }

    public void writeDotaHeroesUpdated(@NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(KEY_DOTA_HEROES_UPDATED, timeStamp);
        editor.apply();
    }

    public long getDotaItemsUpdated() {
        return getSharedPreferences().getLong(KEY_DOTA_ITEMS_UPDATED, Long.MIN_VALUE);
    }

    public void writeDotaItemsUpdated(@NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(KEY_DOTA_ITEMS_UPDATED, timeStamp);
        editor.apply();
    }

    public long getDotaRaritiesUpdated() {
        return getSharedPreferences().getLong(KEY_DOTA_RARITIES_UPDATED, Long.MIN_VALUE);
    }

    public void writeDotaRaritiesUpdated(@NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(KEY_DOTA_RARITIES_UPDATED, timeStamp);
        editor.apply();
    }

    public long getHeroDetailsUpdated() {
        return getSharedPreferences().getLong(KEY_HERO_DETAILS_UPDATED, Long.MIN_VALUE);
    }

    public void writeHeroDetailsUpdated(final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(KEY_HERO_DETAILS_UPDATED, timeStamp);
        editor.apply();
    }

    public long getProfileUpdated(@NonNull final String steamId) {
        return getSharedPreferences().getLong(getUserKeyForId(steamId), Long.MIN_VALUE);
    }

    public void writeProfileUpdated(@NonNull final String steamId, @NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(getUserKeyForId(steamId), timeStamp);
        editor.apply();
    }

    public long getLeaguesUpdated() {
        return getSharedPreferences().getLong(KEY_DOTA_LEAGUES_UPDATED, Long.MIN_VALUE);
    }

    public void writeLeaguesUpdated(@NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(KEY_DOTA_LEAGUES_UPDATED, timeStamp);
        editor.apply();
    }

    public long getLibraryUpdated(@NonNull final String steamId) {
        return getSharedPreferences().getLong(getLibraryKeyForId(steamId), Long.MIN_VALUE);
    }

    public void writeLibraryUpdated(@NonNull final String steamId, @NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(getLibraryKeyForId(steamId), timeStamp);
        editor.apply();
    }

    public long getMatchOverviewUpdated(@NonNull final String steamId) {
        return getSharedPreferences().getLong(getMatchOverviewKeyForId(steamId), Long.MIN_VALUE);
    }

    public void writeMatchOverviewUpdated(@NonNull final String steamId, @NonNull final Long timeStamp) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(getMatchOverviewKeyForId(steamId), timeStamp);
        editor.apply();
    }

    private static String getUserKeyForId(final String steamId) {
        return String.format("%s_%s", KEY_PREFIX_USER, steamId);
    }

    private static String getLibraryKeyForId(final String steamId) {
        return String.format("%s_%s", KEY_PREFIX_LIBRARY, steamId);
    }

    private static String getMatchOverviewKeyForId(final String steamId) {
        return String.format("%s_%s", KEY_PREFIX_MATCH_OVERVIEW, steamId);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }
}
