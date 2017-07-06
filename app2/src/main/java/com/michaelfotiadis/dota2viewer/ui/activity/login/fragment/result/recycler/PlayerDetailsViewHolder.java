package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class PlayerDetailsViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.includable_player_details;

    @BindView(R.id.text_label_persona)
    TextView mTextLabelPersona;
    @BindView(R.id.text_content_persona)
    TextView mTextContentPersona;
    @BindView(R.id.table_row_alias)
    TableRow mTableRowAlias;
    @BindView(R.id.text_label_steam_id)
    TextView mTextLabelSteamId;
    @BindView(R.id.text_content_steam_id)
    TextView mTextContentSteamId;
    @BindView(R.id.table_row_id)
    TableRow mTableRowId;
    @BindView(R.id.text_label_privacy)
    TextView mTextLabelPrivacy;
    @BindView(R.id.text_content_privacy)
    TextView mTextContentPrivacy;
    @BindView(R.id.table_row_privacy)
    TableRow mTableRowPrivacySettings;
    @BindView(R.id.text_label_status)
    TextView mTextLabelStatus;
    @BindView(R.id.text_content_status)
    TextView mTextContentStatus;
    @BindView(R.id.table_row_status)
    TableRow mTableRowOnlineStatus;
    @BindView(R.id.text_label_last_online)
    TextView mTextLabelLastOnline;
    @BindView(R.id.text_content_last_online)
    TextView mTextContentLastOnline;
    @BindView(R.id.table_row_last_online)
    TableRow mTableRowLastOnline;
    @BindView(R.id.text_label_number_of_games)
    TextView mTextLabelNumberOfGames;
    @BindView(R.id.text_content_number_of_games)
    TextView mTextContentNumberOfGames;
    @BindView(R.id.table_row_library)
    TableRow mTableRowLibrary;
    @BindView(R.id.text_label_dota_2)
    TextView mTextLabelDota2;
    @BindView(R.id.text_content_hours_on_record)
    TextView mTextContentHoursOnRecord;
    @BindView(R.id.table_row_dota_2)
    TableRow mTableRowDota;
    @BindView(R.id.text_label_url)
    TextView mTextLabelUrl;
    @BindView(R.id.table_row_url_title)
    TableRow mTableRowUrlTitle;
    @BindView(R.id.text_content_url)
    TextView mTextContentUrl;
    @BindView(R.id.table_row_url_content)
    TableRow mTableRowUrlContent;


    protected PlayerDetailsViewHolder(final View view) {
        super(view);
    }
}
