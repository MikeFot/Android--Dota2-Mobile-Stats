package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;

public final class PredefinedAnimations {
    public static final ItemAnimationCustomizer FADE_IN = new ItemAnimationCustomizer() {
        @Override
        void onCancel(final RecyclerView.ViewHolder holder) {
            ViewCompat.setAlpha(holder.itemView, 1);
        }

        @Override
        void onEnd(final RecyclerView.ViewHolder viewHolder) {
            // NOOP
        }

        @Override
        ViewPropertyAnimatorCompat onPrepare(final ViewPropertyAnimatorCompat animation, final RecyclerView.ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 0);
            animation.alpha(1);
            return animation;
        }
    };

    public static final ItemAnimationCustomizer FADE_OUT = new ItemAnimationCustomizer() {
        @Override
        void onEnd(final RecyclerView.ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 1);
        }

        @Override
        ViewPropertyAnimatorCompat onPrepare(final ViewPropertyAnimatorCompat animation, final RecyclerView.ViewHolder viewHolder) {
            animation.alpha(0);
            return animation;
        }
    };

    public static final ItemAnimationCustomizer GROW = new ItemAnimationCustomizer() {
        @Override
        void onCancel(final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setScaleX(1.0f);
            viewHolder.itemView.setScaleY(1.0f);
            ViewCompat.setAlpha(viewHolder.itemView, 1);
        }

        @Override
        void onEnd(final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setScaleX(1.0f);
            viewHolder.itemView.setScaleY(1.0f);
        }

        @Override
        ViewPropertyAnimatorCompat onPrepare(final ViewPropertyAnimatorCompat animation, final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setScaleX(0.25f);
            viewHolder.itemView.setScaleY(0.25f);
            animation.scaleX(1.0f);
            animation.scaleY(1.0f);
            ViewCompat.setAlpha(viewHolder.itemView, 0);
            animation.alpha(1);
            return animation;
        }
    };

    public static final ItemAnimationCustomizer SHRINK = new ItemAnimationCustomizer() {
        @Override
        public void onEnd(final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setScaleX(1.0f);
            viewHolder.itemView.setScaleY(1.0f);
        }

        @Override
        ViewPropertyAnimatorCompat onPrepare(final ViewPropertyAnimatorCompat animation,
                                             final RecyclerView.ViewHolder viewHolder) {
            animation.scaleX(0.25f);
            animation.scaleY(0.25f);
            return animation;
        }
    };

    private PredefinedAnimations() {
    }
}
