package com.michaelfotiadis.mobiledota2.ui.view.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import com.michaelfotiadis.mobiledota2.R;

/**
 *
 */
/*package*/ final class ViewAnimator {
    private static final int DEFAULT_SPEED_FACTOR = 5;

    private ViewAnimator() {
        // NOOP
    }

    public static void collapse(final View v) {
        collapse(v, DEFAULT_SPEED_FACTOR);
    }

    public static void collapse(final View v, final int speedFactor) {
        final int initialHeight = v.getMeasuredHeight();

        final Animation a = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(final float interpolatedTime, final Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }


        };

        a.setDuration(calcDuration(v, initialHeight, speedFactor));
        v.startAnimation(a);
    }

    static void expand(final View v) {
        expand(v, DEFAULT_SPEED_FACTOR);
    }

    static void expand(final View v, final int speedFactor) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        final Animation a = new Animation() {
            @Override
            protected void applyTransformation(final float interpolatedTime, final Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(calcDuration(v, targetHeight, speedFactor));
        v.startAnimation(a);
    }

    static void slideUpFromBottom(final View v) {
        v.setVisibility(View.VISIBLE);
        final Animation a = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up_bottom);
        a.reset();
        v.clearAnimation();
        v.startAnimation(a);
    }

    static void slideOut(final View v) {
        final Animation a = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_top);
        a.reset();
        v.clearAnimation();
        v.startAnimation(a);
    }

    static void slideFromRight(final View v) {
        v.setVisibility(View.VISIBLE);
        final Animation a = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_in_right);
        a.reset();
        v.clearAnimation();
        v.startAnimation(a);
    }

    private static int calcDuration(final View view, final int height, final int speedFactor) {
        // 1dp/ms for speedFactor==1
        return (int) (height / view.getContext().getResources().getDisplayMetrics().density) * speedFactor;
    }


}
