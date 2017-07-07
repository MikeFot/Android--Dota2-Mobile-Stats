package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.recycler;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.OnUserSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.PlayerWrapper;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch.IntentDispatcherImpl;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.ViewUtils;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.mobiledota2.utils.country.CountryUtils;
import com.michaelfotiadis.mobiledota2.utils.date.DateUtils;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import java.util.concurrent.TimeUnit;

public class PlayerViewBinder extends BaseRecyclerViewBinder<PlayerViewHolder, PlayerWrapper> {

    private final ImageLoader mImageLoader;
    private final OnUserSelectedListener mOnUserSelectedListener;

    PlayerViewBinder(final Context context,
                     final ImageLoader imageLoader,
                     final OnUserSelectedListener onUserSelectedListener) {
        super(context);
        mImageLoader = imageLoader;
        mOnUserSelectedListener = onUserSelectedListener;
    }

    @Override
    protected void reset(final PlayerViewHolder holder) {

    }

    @Override
    protected void setData(final PlayerViewHolder holder, final PlayerWrapper item) {


        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mOnUserSelectedListener.onUserSelected(v, item.getPlayerSummary());
            }
        });

        populateHeader(holder.getHeaderViewHolder(), item.getPlayerSummary());
        populateDetails(holder.getDetailsViewHolder(), item);

    }


    protected void populateHeader(final PlayerHeaderViewHolder viewHolder, final PlayerSummary item) {
        if (item == null) {
            AppLog.e("Null User object (header)");
            return;
        }
        viewHolder.mTextPersona.setText(item.getPersonaName());

        if (TextUtils.isEmpty(item.getLocCountryCode())) {
            viewHolder.mTextCountry.setText("");
        } else {
            viewHolder.mTextCountry.setText(CountryUtils.getCountryNameFromCountryCode(item.getLocCountryCode()));
        }

        if (TextUtils.isNotEmpty(item.getAvatarMedium())) {
            mImageLoader.loadIntoImageView(viewHolder.mImagePlayerIcon, item.getAvatarMedium());
        }

        if (item.getLocCountryCode() != null) {
            mImageLoader.loadIntoImageView(viewHolder.mImagePlayerFlag, item.getLocCountryCode(), ImageLoader.ImageFamily.COUNTRY);
            viewHolder.mImagePlayerFlag.setContentDescription(String.format(getString(R.string.ct_flag_placeholder), item.getLocCountryCode()));
        }
    }

    private void populateDetails(final PlayerDetailsViewHolder viewHolder, final PlayerWrapper wrapper) {
        if (wrapper == null) {
            AppLog.e("Null User object (body)");
            return;
        }

        final PlayerSummary summary = wrapper.getPlayerSummary();

        viewHolder.mTextContentSteamId.setText(summary.getSteamId());
        viewHolder.mTextContentPersona.setText(summary.getPersonaName());
        // set the date of last online (ignore hours)
        final String lastOnline = DateUtils.getDateFromUnixTime(
                TimeUnit.SECONDS.toMillis(summary.getLastLogOff()),
                getString(R.string.date_format_account_view));
        viewHolder.mTextContentLastOnline.setText(lastOnline);

        if (TextUtils.isNotEmpty(summary.getProfileUrl())) {
            final SpannableString link = new SpannableString(summary.getProfileUrl());
            link.setSpan(new UnderlineSpan(), 0, link.length(), 0);

            viewHolder.mTextContentUrl.setText(link);
            viewHolder.mTextContentUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new IntentDispatcherImpl(getContext()).open(v, Uri.parse(summary.getProfileUrl()));
                }
            });
        } else {
            ViewUtils.showView(viewHolder.mTableRowUrlTitle, false);
            ViewUtils.showView(viewHolder.mTableRowUrlContent, false);
        }


        // set up the account status with textViewHeroName and color
        viewHolder.mTextContentStatus.setText(getOnlineStatus(summary.getPersonaState()));
        viewHolder.mTextContentStatus.setTextColor(getColor(getOnlineColor(summary.getPersonaState())));

        // set up the privacy status with textViewHeroName and color
        viewHolder.mTextContentPrivacy.setText(getPrivacyStatus(summary.getCommunityVisibilityState()));
        viewHolder.mTextContentPrivacy.setTextColor(getPrivacyColor(summary.getCommunityVisibilityState()));

        ViewUtils.showView(viewHolder.mTableRowLibrary, false);

        ViewUtils.showView(viewHolder.mTableRowDota, wrapper.getDotaAvailable() != null);
        if (wrapper.getDotaAvailable() != null) {
            final int tintColor = wrapper.getDotaAvailable() ? R.color.md_green_500 : R.color.md_red_500;
            final String dotaMessage = wrapper.getDotaAvailable() ? getString(R.string.dota_data_available) : getString(R.string.dota_data_unavailable);
            viewHolder.mTextContentHoursOnRecord.setText(dotaMessage);
            viewHolder.mTextContentHoursOnRecord.setTextColor(getColor(tintColor));
        }

    }

    @NonNull
    private String getOnlineStatus(final int code) {
        //The user's current status. 0 - Offline, 1 - Online, 2 - Busy, 3 - Away,
        // 4 - Snooze, 5 - looking to trade, 6 - looking to play. If the player's profile is private, this will always be "0",
        // TODO Replace with Array
        switch (code) {
            case 0:
                return getString(R.string.text_status_offline);
            case 1:
                return getString(R.string.text_status_online);
            case 2:
                return getString(R.string.text_status_busy);
            case 3:
                return getString(R.string.text_status_away);
            case 4:
                return getString(R.string.text_status_snooze);
            case 5:
                return getString(R.string.text_status_trade);
            case 6:
                return getString(R.string.text_status_looking);
            default:
                return getString(R.string.text_status_offline);
        }
    }


    private int getPrivacyColor(final int code) {
        return code == 1 ? getColor(R.color.md_red_500) : getColor(R.color.md_green_500);
    }

    @ColorRes
    private int getOnlineColor(final int code) {
        //The user's current status. 0 - Offline, 1 - Online, 2 - Busy, 3 - Away,
        // 4 - Snooze, 5 - looking to trade, 6 - looking to play. If the player's profile is private, this will always be "0",

        switch (code) {
            case 0:
                return R.color.md_grey_600;
            case 1:
                return R.color.md_light_green_500;
            case 2:
                return R.color.md_orange_300;
            case 3:
                return R.color.md_red_500;
            case 4:
                return R.color.md_purple_300;
            case 5:
                return R.color.md_blue_500;
            case 6:
                return R.color.md_green_500;
            default:
                return R.color.md_light_blue_200;
        }
    }

    private String getPrivacyStatus(final int code) {
        return code == 1 ? getString(R.string.text_steam_privacy_private) : getString(R.string.text_steam_privacy_public);
    }

}
