package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.michaelfotiadis.dota2viewer.R;

import butterknife.BindView;

class PieChartViewHolder extends BaseChartViewHolder {

    @BindView(R.id.chart_view)
    PieChart mChart;

    PieChartViewHolder(final View itemView) {
        super(itemView);
    }

    @LayoutRes
    public static int getLayoutId() {
        return R.layout.list_item_pie_chart;
    }

}