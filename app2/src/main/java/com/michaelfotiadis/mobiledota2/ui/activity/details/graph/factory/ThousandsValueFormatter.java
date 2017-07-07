package com.michaelfotiadis.mobiledota2.ui.activity.details.graph.factory;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

class ThousandsValueFormatter implements ValueFormatter {
    @Override
    public String getFormattedValue(final float value) {
        return new DecimalFormat("#0").format(value / 1000) + "k";
    }
}