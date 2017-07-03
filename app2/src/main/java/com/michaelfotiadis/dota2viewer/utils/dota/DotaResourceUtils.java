package com.michaelfotiadis.dota2viewer.utils.dota;

import android.support.annotation.StringRes;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.steam.data.dota2.types.GameMode;
import com.michaelfotiadis.steam.data.dota2.types.LeaverStatus;
import com.michaelfotiadis.steam.data.dota2.types.LobbyType;

import org.apache.commons.lang3.text.WordUtils;

public final class DotaResourceUtils {

    private DotaResourceUtils() {
    }

    @StringRes
    public static int getDescriptionForGameMode(final GameMode gameMode) {

        @StringRes final int resId;
        switch (gameMode) {
            case ALL_PICK:
                resId = R.string.game_mode_all_pick;
                break;
            case CAPTAINS_MODE:
                resId = R.string.game_mode_captains_mode;
                break;
            case RANDOM_DRAFT:
                resId = R.string.game_mode_random_draft;
                break;
            case SINGLE_DRAFT:
                resId = R.string.game_mode_single_draft;
                break;
            case ALL_RANDOM:
                resId = R.string.game_mode_all_random;
                break;
            case INTRO_DEATH:
                resId = R.string.game_mode_intro_death;
                break;
            case THE_DIRETIDE:
                resId = R.string.game_mode_diretide;
                break;
            case REVERSE_CAPTAINS_MODE:
                resId = R.string.game_mode_reverse_captains_mode;
                break;
            case GREEVILING:
                resId = R.string.game_mode_greeviling;
                break;
            case TUTORIAL:
                resId = R.string.game_mode_tutorial;
                break;
            case MID_ONLY:
                resId = R.string.game_mode_mid_only;
                break;
            case LEAST_PLAYED:
                resId = R.string.game_mode_least_played;
                break;
            case NEW_PLAYER_POOL:
                resId = R.string.game_mode_new_player_pool;
                break;
            case COMPENDIUM_MATCHING:
                resId = R.string.game_mode_compendium_watching;
                break;
            case CUSTOM:
                resId = R.string.game_mode_custom;
                break;
            case CAPTAINS_DRAFT:
                resId = R.string.game_mode_captains_draft;
                break;
            case BALANCED_DRAFT:
                resId = R.string.game_mode_balanced_draft;
                break;
            case ABILITY_DRAFT:
                resId = R.string.game_mode_ability_draft;
                break;
            case EVENT:
                resId = R.string.game_mode_event;
                break;
            case ALL_RANDOM_DEATH_MATCH:
                resId = R.string.game_mode_all_random_death_match;
                break;
            case ONEVSONE_SOLO_MID:
                resId = R.string.game_mode_solo_mid;
                break;
            case ALL_DRAFT:
                resId = R.string.game_mode_all_draft;
                break;
            default:
                resId = R.string.game_mode_unknown;
        }
        return resId;

    }


    @StringRes
    public static int getDescriptionForLobbyType(final LobbyType lobbyType) {

        @StringRes final int resId;
        switch (lobbyType) {

            case PUBLIC_MATCHMAKING:
                resId = R.string.lobby_type_public_matchmaking;
                break;
            case PRACTICE:
                resId = R.string.lobby_type_practice;
                break;
            case TOURNAMENT:
                resId = R.string.lobby_type_tournament;
                break;
            case TUTORIAL:
                resId = R.string.lobby_type_tutorial;
                break;
            case COOP_WITH_BOTS:
                resId = R.string.lobby_type_coop_with_bots;
                break;
            case TEAM_MATCH:
                resId = R.string.lobby_type_team_match;
                break;
            case SOLO_QUEUE:
                resId = R.string.lobby_type_solo_queue;
                break;
            case RANKED:
                resId = R.string.lobby_type_ranked;
                break;
            case SOLO_MID_ONEVSONE:
                resId = R.string.lobby_type_solo_mid;
                break;
            default:
                resId = R.string.lobby_type_unknown;
        }

        return resId;
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

    public static String getHeroNameFromDataName(final String dataName) {

        final String result = dataName.replaceAll("npc_dota_hero_", "").replaceAll("_", " ");
        return WordUtils.capitalizeFully(result);

    }
}
