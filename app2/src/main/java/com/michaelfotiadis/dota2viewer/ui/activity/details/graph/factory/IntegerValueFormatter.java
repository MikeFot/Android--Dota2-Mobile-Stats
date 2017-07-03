package com.michaelfotiadis.dota2viewer.ui.activity.details.graph.factory;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

class IntegerValueFormatter implements ValueFormatter {
    @Override
    public String getFormattedValue(final float value) {
        return new DecimalFormat("#0").format(value);
    }
}