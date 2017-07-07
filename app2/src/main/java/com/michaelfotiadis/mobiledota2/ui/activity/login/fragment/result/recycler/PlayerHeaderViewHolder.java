package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.result.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class PlayerHeaderViewHolder extends BaseRecyclerViewHolder {

    @LayoutRes
    private static final int LAYOUT_ID = R.layout.includable_player_header;
    @BindView(R.id.image_player_icon)
    ImageView mImagePlayerIcon;
    @BindView(R.id.text_persona)
    TextView mTextPersona;
    @BindView(R.id.image_player_flag)
    ImageView mImagePlayerFlag;
    @BindView(R.id.text_country)
    TextView mTextCountry;
    @BindView(R.id.layout_country)
    LinearLayout mLayoutCountry;

    protected PlayerHeaderViewHolder(final View view) {
        super(view);
    }
}
