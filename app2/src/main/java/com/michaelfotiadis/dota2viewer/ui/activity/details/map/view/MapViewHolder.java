package com.michaelfotiadis.dota2viewer.ui.activity.details.map.view;

import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.ui.core.base.view.holder.BaseViewHolder;
import com.michaelfotiadis.dota2viewer.ui.view.map.ErgoDotaMapView;

import butterknife.BindView;

/**
 * Created by Map on 07/06/2015.
 */
public class MapViewHolder extends BaseViewHolder {
    @BindView(R.id.map_view)
    public ErgoDotaMapView mapView;

    public MapViewHolder(final View rootView) {
        super(rootView);
    }
}
