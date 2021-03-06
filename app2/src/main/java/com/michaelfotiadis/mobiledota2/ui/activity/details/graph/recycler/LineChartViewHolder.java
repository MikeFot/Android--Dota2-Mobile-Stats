package com.michaelfotiadis.mobiledota2.ui.activity.details.graph.recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.michaelfotiadis.mobiledota2.R;

import butterknife.BindView;

class LineChartViewHolder extends BaseChartViewHolder {
    @BindView(R.id.chart_view)
    LineChart mChart;

    LineChartViewHolder(final View itemView) {
        super(itemView);
    }

    @LayoutRes
    public static int getLayoutId() {
        return R.layout.list_item_line_chart;
    }

}