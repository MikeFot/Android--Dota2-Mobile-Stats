package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

public class PlayerViewHolder extends BaseRecyclerViewHolder {


    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_profile;

    private final PlayerDetailsViewHolder mDetailsViewHolder;
    private final PlayerHeaderViewHolder mHeaderViewHolder;

    protected PlayerViewHolder(final View view) {
        super(view);
        mDetailsViewHolder = new PlayerDetailsViewHolder(view);
        mHeaderViewHolder = new PlayerHeaderViewHolder(view);
    }

    public PlayerHeaderViewHolder getHeaderViewHolder() {
        return mHeaderViewHolder;
    }

    public PlayerDetailsViewHolder getDetailsViewHolder() {
        return mDetailsViewHolder;
    }

    @LayoutRes
    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}
