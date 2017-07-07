package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.recycler;

import android.content.Context;
import android.view.View;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular.PopularPlayer;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.steam.utils.SteamIdUtils;

class PopularPlayersViewBinder extends BaseRecyclerViewBinder<PopularPlayersViewHolder, PopularPlayer> {

    private final OnItemSelectedListener<PopularPlayer> mListener;

    PopularPlayersViewBinder(final Context context,
                             final OnItemSelectedListener<PopularPlayer> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void reset(final PopularPlayersViewHolder holder) {
        holder.mTextName.setText("");
        holder.mTextId64.setText("");
        holder.mTextId3.setText("");
        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final PopularPlayersViewHolder holder, final PopularPlayer item) {


        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });

        holder.mTextName.setText(item.getName());

        // try to parse the IDs
        holder.mTextId64.setText(getContext().getString(R.string.id_64, SteamIdUtils.getId64FromString(item.getId())));
        holder.mTextId3.setText(getContext().getString(R.string.id_3, SteamIdUtils.getId3FromString(item.getId())));

    }


}
