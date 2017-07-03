package com.michaelfotiadis.dota2viewer.ui.view.utils;

import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimpleSpanBuilder {

    private final List<SpanSection> spanSections;
    private final StringBuilder stringBuilder;

    public SimpleSpanBuilder() {
        stringBuilder = new StringBuilder();
        spanSections = new ArrayList<>();
    }

    public SimpleSpanBuilder append(final String text, final ParcelableSpan... spans) {
        if (spans != null && spans.length > 0) {
            spanSections.add(new SpanSection(text, stringBuilder.length(), spans));
        }
        stringBuilder.append(text);
        return this;
    }

    public SpannableStringBuilder build() {
        final SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuilder.toString());
        for (final SpanSection section : spanSections) {
            section.apply(ssb);
        }
        return ssb;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private static class SpanSection {
        private final String text;
        private final int startIndex;
        private final ParcelableSpan[] spans;

        public SpanSection(final String text, final int startIndex, final ParcelableSpan... spans) {
            this.spans = spans;
            this.text = text;
            this.startIndex = startIndex;
        }

        public void apply(final SpannableStringBuilder spanStringBuilder) {
            if (spanStringBuilder == null) return;
            for (final ParcelableSpan span : spans) {
                spanStringBuilder.setSpan(span, startIndex, startIndex + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }
}