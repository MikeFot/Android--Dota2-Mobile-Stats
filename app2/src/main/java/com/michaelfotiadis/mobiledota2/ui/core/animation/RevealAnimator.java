package com.michaelfotiadis.mobiledota2.ui.core.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.michaelfotiadis.mobiledota2.R;


public class RevealAnimator {
    private static final boolean USE_REVEAL = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;

    public static void show(@NonNull final View myView) {
        if (USE_REVEAL) {
            // get the center for the clipping circle
            final int cx = myView.getWidth() / 2;
            final int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            final float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            final Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            final Animation anim = AnimationUtils.loadAnimation(myView.getContext(), R.anim.fade_in);
            myView.startAnimation(anim);
        }
    }

    public static void hide(@NonNull final View view) {
        if (USE_REVEAL) {
            // get the center for the clipping circle
            final int cx = view.getWidth() / 2;
            final int cy = view.getHeight() / 2;

            // get the initial radius for the clipping circle
            final float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            final Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        } else {
            final Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
            view.startAnimation(anim);
        }
    }
}
