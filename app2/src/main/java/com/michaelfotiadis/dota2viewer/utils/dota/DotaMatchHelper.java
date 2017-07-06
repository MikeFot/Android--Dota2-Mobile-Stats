package com.michaelfotiadis.dota2viewer.utils.dota;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelfotiadis.dota2viewer.ui.activity.details.MatchContainer;
import com.michaelfotiadis.dota2viewer.utils.format.FormattingUtils;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;
import com.michaelfotiadis.steam.data.dota2.model.player.PlayerDetails;
import com.michaelfotiadis.steam.utils.SteamIdUtils;

/**
 * Created by Map on 04/06/2015.
 */
public class DotaMatchHelper {

    private final long mAccountId;
    private final PlayerDetails mPlayer;
    private final MatchDetails mMatch;

    public DotaMatchHelper(@NonNull final MatchContainer matchContainer) {
        this(matchContainer.getUserId3(), matchContainer.getMatchDetails());
    }

    public DotaMatchHelper(final long playerId, @NonNull final MatchDetails match) {
        this.mAccountId = SteamIdUtils.isSteamId64(playerId) ? SteamIdUtils.steamId64toSteamId3(playerId) : playerId;
        this.mMatch = match;
        this.mPlayer = getUserPlayerFromMatch(match);
    }

    public MatchDetails getMatch() {
        return mMatch;
    }

    @Nullable
    public PlayerDetails getPlayer() {
        return mPlayer;
    }

    public long getAccountId() {
        return mAccountId;
    }


    public boolean isPlayerVictorious() {
        if (mPlayer == null) {
            return false;
        } else if (mPlayer.getPlayerSlot() < 5) {
            return mMatch.getRadiantWin();
        } else {
            return !mMatch.getRadiantWin();
        }
    }

    public int getPlayerKills() {
        return mPlayer != null ? mPlayer.getKills() : 0;
    }

    public int getPlayerDeaths() {
        return mPlayer != null ? mPlayer.getDeaths() : 0;
    }

    public int getPlayerAssists() {
        return mPlayer != null ? mPlayer.getAssists() : 0;
    }

    @Nullable
    private PlayerDetails getUserPlayerFromMatch(final MatchDetails match) {
        for (final PlayerDetails player : match.getPlayers()) {
            if (player.getAccountId() == mAccountId) {
                return player;
            }
        }
        return null;
    }


    public String getDurationForMatch() {
        int seconds = mMatch.getDuration();

        final int hours = seconds / 3600;
        final int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        final StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(FormattingUtils.formatTwoDigitString(hours));
            sb.append(":");
        }
        sb.append(FormattingUtils.formatTwoDigitString(minutes));
        sb.append(":");
        sb.append(FormattingUtils.formatTwoDigitString(seconds));

        return sb.toString();
    }

}
