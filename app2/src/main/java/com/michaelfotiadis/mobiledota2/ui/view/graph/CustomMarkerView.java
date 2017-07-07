package com.michaelfotiadis.mobiledota2.ui.view.graph;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;
import com.michaelfotiadis.mobiledota2.R;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class CustomMarkerView extends MarkerView {

    private TextView mTextView;

    public CustomMarkerView(final Context context, final int layoutResource) {
        super(context, layoutResource);

        mTextView = (TextView) findViewById(R.id.text_content);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(final Entry e, final int dataSetIndex) {

        if (e instanceof CandleEntry) {

            final CandleEntry ce = (CandleEntry) e;

            mTextView.setText(String.valueOf(Utils.formatNumber(ce.getHigh(), 0, true)));
        } else {

            mTextView.setText(String.valueOf(Utils.formatNumber(e.getVal(), 0, true)));
        }
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}