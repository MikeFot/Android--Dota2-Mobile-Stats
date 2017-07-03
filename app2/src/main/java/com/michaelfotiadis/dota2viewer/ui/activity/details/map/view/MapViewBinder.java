package com.michaelfotiadis.dota2viewer.ui.activity.details.map.view;

import android.content.Context;

import com.michaelfotiadis.dota2viewer.ui.core.base.view.binder.BaseViewBinder;
import com.michaelfotiadis.dota2viewer.ui.view.map.MapManager;
import com.michaelfotiadis.steam.data.dota2.model.match.details.MatchDetails;

public class MapViewBinder extends BaseViewBinder<MapViewHolder, MatchDetails> {

    public MapViewBinder(final Context context) {
        super(context);
    }

    @Override
    protected void setData(final MapViewHolder holder, final MatchDetails item) {

        final MapManager manager = new MapManager();
        manager.setUpRadiantTowers(item.getTowerStatusRadiant());
        manager.setUpDireTowers(item.getTowerStatusDire());
        manager.setUpRadiantRax(item.getBarracksStatusRadiant());
        manager.setUpDireRax(item.getBarracksStatusDire());

        holder.mapView.setMapList(manager.getAsList());
    }
}
