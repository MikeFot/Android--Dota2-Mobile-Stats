package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.factory;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.RadarData;
import com.michaelfotiadis.dota2viewer.ui.view.graph.CustomMarkerView;

@SuppressWarnings("MethodMayBeStatic")
public class DotaGeneralGraphFactory {

    public void setUpLineChart(final LineChart lineChart,
                               final LineData data,
                               final CustomMarkerView mMarkerView) {
        // no description text
        lineChart.setDescription("Total Levels/Minute");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        lineChart.setHighlightEnabled(true);

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setMarkerView(mMarkerView);
        lineChart.setDragDecelerationFrictionCoef(0.9f);
        lineChart.setDescriptionColor(Color.BLACK);
        lineChart.setDescriptionTextSize(12f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        lineChart.setData(data);

        lineChart.animateX(2500);

        // get the legend (only possible after setting data)
        final Legend legend = lineChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        legend.setTextColor(Color.BLACK);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setYOffset(11f);

        final XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        final YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(data.getYMax());
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new IntegerValueFormatter());

        final YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setAxisMaxValue(data.getYMax());
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setDrawGridLines(true);
        rightAxis.setValueFormatter(new IntegerValueFormatter());
    }

    public void setUpPieChart(final PieChart pieChart,
                              final PieData data,
                              final String centerText,
                              final int centerColor) {
        pieChart.setUsePercentValues(false);
        pieChart.setDescription("");

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);

        pieChart.setTransparentCircleColor(Color.BLACK);
        pieChart.setCenterTextColor(centerColor);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setCenterText(centerText);


        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateX(1000);
        pieChart.spin(1000, 300, 360, Easing.EasingOption.EaseInBack);

    }

    public void setUpHorizontalBarChart(final HorizontalBarChart barChart,
                                        final BarData data,
                                        final int backgroundColor) {

        barChart.setDescription("");
        barChart.setGridBackgroundColor(backgroundColor);

        // if false values are only drawn for the stack sum, else each value is drawn
        barChart.setDrawValuesForWholeStack(false);
        // disable touch
        barChart.setTouchEnabled(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        final XAxis axisX1 = barChart.getXAxis();
        axisX1.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisX1.setDrawAxisLine(true);
        axisX1.setDrawGridLines(true);
        axisX1.setGridLineWidth(0.3f);
        axisX1.setTextColor(Color.BLACK);

        final YAxis axisY1 = barChart.getAxisLeft();
        axisY1.setDrawAxisLine(true);
        axisY1.setDrawGridLines(true);
        axisY1.setGridLineWidth(0.3f);
        axisY1.setTextColor(Color.BLACK);
        axisY1.setValueFormatter(new ThousandsValueFormatter());
//        yl.setInverted(true);

        final YAxis axisY2 = barChart.getAxisRight();
        axisY2.setDrawAxisLine(true);
        axisY2.setDrawGridLines(false);
        axisY2.setTextColor(Color.BLACK);
        axisY2.setValueFormatter(new ThousandsValueFormatter());
//        yr.setInverted(true);

        barChart.animateY(2500);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setData(data);
        // lineChart.setDrawXLabels(false);
        // lineChart.setDrawYLabels(false);

        final Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(8f);
        legend.setTextColor(Color.BLACK);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);
    }

    public void setUpRadarChart(final RadarChart radarChart,
                                final RadarData data,
                                final MarkerView markerView) {
        radarChart.setDescription("");

        radarChart.setWebLineWidth(1.5f);
        radarChart.setWebLineWidthInner(0.75f);
        radarChart.setWebAlpha(100);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebColorInner(Color.GRAY);
        radarChart.setDescriptionColor(Color.BLACK);

        // set the marker to the chart
        radarChart.setMarkerView(markerView);
        radarChart.setRotationEnabled(true);
        data.setDrawValues(true);
        radarChart.setData(data);

        final XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(14f);
//        xAxis.setYOffset(-10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextColor(Color.BLACK);

        final YAxis yAxis = radarChart.getYAxis();
        yAxis.setEnabled(false);
        yAxis.setTextSize(20f);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setDrawTopYLabelEntry(false);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setStartAtZero(true);

        final Legend legend = radarChart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(8f);
        legend.setTextColor(Color.BLACK);
        radarChart.invalidate();
    }

}
