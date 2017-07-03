package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.animation;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;

/*package*/ abstract class ItemAnimationCustomizer {
    void onCancel(final RecyclerView.ViewHolder holder) {
    }

    void onEnd(final RecyclerView.ViewHolder viewHolder) {
    }

    abstract ViewPropertyAnimatorCompat onPrepare(ViewPropertyAnimatorCompat animation, RecyclerView.ViewHolder viewHolder);
}
