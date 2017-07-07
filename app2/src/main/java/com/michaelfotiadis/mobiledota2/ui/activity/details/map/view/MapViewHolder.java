package com.michaelfotiadis.mobiledota2.ui.activity.details.map.view;

import android.view.View;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.view.holder.BaseViewHolder;
import com.michaelfotiadis.mobiledota2.ui.view.map.DotaMapView;

import butterknife.BindView;

/**
 * Created by Map on 07/06/2015.
 */
public class MapViewHolder extends BaseViewHolder {
    @BindView(R.id.map_view)
    public DotaMapView mapView;

    public MapViewHolder(final View rootView) {
        super(rootView);
    }
}
