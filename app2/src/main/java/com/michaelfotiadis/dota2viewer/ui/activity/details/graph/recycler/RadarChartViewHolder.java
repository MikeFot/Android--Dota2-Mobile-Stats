package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.github.mikephil.charting.charts.RadarChart;
import com.michaelfotiadis.dota2viewer.R;

import butterknife.BindView;

class RadarChartViewHolder extends BaseChartViewHolder {
    @BindView(R.id.chart_view)
    RadarChart mChart;

    RadarChartViewHolder(final View itemView) {
        super(itemView);
    }

    @LayoutRes
    public static int getLayoutId() {
        return R.layout.list_item_radar_chart;
    }

}