package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.michaelfotiadis.dota2viewer.R;

import butterknife.BindView;

class BarChartViewHolder extends BaseChartViewHolder {
    @BindView(R.id.chart_view)
    HorizontalBarChart mChart;

    BarChartViewHolder(final View itemView) {
        super(itemView);
    }

    @LayoutRes
    public static int getLayoutId() {
        return R.layout.list_item_bar_chart;
    }

}