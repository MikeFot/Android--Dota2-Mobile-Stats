package com.michaelfotiadis.mobiledota2.ui.activity.details.match.view;

import android.view.View;
import android.widget.LinearLayout;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.view.holder.BaseViewHolder;

import butterknife.BindView;

public class FactionViewHolder extends BaseViewHolder {
    @BindView(R.id.row_header)
    public LinearLayout layoutHeader;
    @BindView(R.id.row_1)
    public LinearLayout layoutRow1;
    @BindView(R.id.row_2)
    public LinearLayout layoutRow2;
    @BindView(R.id.row_3)
    public LinearLayout layoutRow3;
    @BindView(R.id.row_4)
    public LinearLayout layoutRow4;
    @BindView(R.id.row_5)
    public LinearLayout layoutRow5;
    @BindView(R.id.row_footer)
    public LinearLayout layoutFooter;

    public FactionViewHolder(final View rootView) {
        super(rootView);
    }
}