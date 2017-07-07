package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

/*private view mHolder class*/
class GameViewHolder extends BaseRecyclerViewHolder {
    @LayoutRes
    private static final int LAYOUT_ID = R.layout.list_item_steam_game;
    @BindView(R.id.image_game_icon)
    ImageView gameIcon;
    @BindView(R.id.text_content_game_title)
    TextView title;
    @BindView(R.id.text_label_recent_time_played)
    TextView timePlayed;
    @BindView(R.id.text_label_total_time_played)
    TextView totalTimePlayed;

    protected GameViewHolder(final View view) {
        super(view);
    }

    public static int getLayoutId() {
        return LAYOUT_ID;
    }
}