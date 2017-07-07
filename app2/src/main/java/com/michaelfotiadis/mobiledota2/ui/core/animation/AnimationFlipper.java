package com.michaelfotiadis.mobiledota2.ui.core.animation;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.widget.ViewAnimator;

import com.michaelfotiadis.mobiledota2.ui.core.animation.flip.FlipAnimation;
import com.michaelfotiadis.mobiledota2.ui.core.animation.flip.FlipDirection;

/**
 *
 */

public final class AnimationFlipper {

    private AnimationFlipper() {
        // do not instantiate
    }

    /**
     * Create a pair of {@link FlipAnimation} that can be used to flip 3D transition from {@code fromView} to {@code toView}. A typical use case is with {@link ViewAnimator} as an out and in transition.
     *
     * @param fromView     the view transition away from
     * @param toView       the view transition to
     * @param dir          the flip direction
     * @param duration     the transition duration in milliseconds
     * @param interpolator the interpolator to use (pass {@code null} to use the {@link AccelerateInterpolator} interpolator)
     * @return {@link Animation} array
     */
    private static Animation[] flipAnimation(final View fromView, final View toView, final FlipDirection dir, final long duration, final Interpolator interpolator) {
        final Animation[] result = new Animation[2];
        final float centerX;
        final float centerY;

        centerX = fromView.getWidth() / 2.0f;
        centerY = fromView.getHeight() / 2.0f;

        final Animation outFlip = new FlipAnimation(dir.getStartDegreeForFirstView(), dir.getEndDegreeForFirstView(), centerX, centerY, FlipAnimation.SCALE_DEFAULT, FlipAnimation.ScaleUpDownEnum.SCALE_DOWN);
        outFlip.setDuration(duration);
        outFlip.setFillAfter(true);
        outFlip.setInterpolator(interpolator == null ? new AccelerateInterpolator() : interpolator);

        final AnimationSet outAnimation = new AnimationSet(true);
        outAnimation.addAnimation(outFlip);
        result[0] = outAnimation;

        // Uncomment the following if toView has its layout established (not the case if using ViewFlipper and on first show)
        //centerX = toView.getWidth() / 2.0f;
        //centerY = toView.getHeight() / 2.0f;

        final Animation inFlip = new FlipAnimation(dir.getStartDegreeForSecondView(), dir.getEndDegreeForSecondView(), centerX, centerY, FlipAnimation.SCALE_DEFAULT, FlipAnimation.ScaleUpDownEnum.SCALE_UP);
        inFlip.setDuration(duration);
        inFlip.setFillAfter(true);
        inFlip.setInterpolator(interpolator == null ? new AccelerateInterpolator() : interpolator);
        inFlip.setStartOffset(duration);

        final AnimationSet inAnimation = new AnimationSet(true);
        inAnimation.addAnimation(inFlip);
        result[1] = inAnimation;

        return result;

    }

    /**
     * Flips a view animator in the direction given
     *
     * @param viewAnimator Animator containing the views (eg ViewFlipper)
     * @param dir          Direction to apply
     * @param duration     Duration of the animation in milliseconds
     * @param targetIndex  Page to show after the animation
     * @param interpolator {@link Interpolator} to be used
     */
    public static Animation flipView(final ViewAnimator viewAnimator, final FlipDirection dir, final long duration, final int targetIndex, @Nullable final Interpolator interpolator) {

        final View fromView = viewAnimator.getCurrentView();
        final int currentIndex = viewAnimator.getDisplayedChild();

        if (currentIndex == targetIndex) {
            return null;
        }

        final View toView = viewAnimator.getChildAt(targetIndex);

        final Animation[] animation = flipAnimation(fromView, toView, (targetIndex < currentIndex ? dir.theOtherDirection() : dir), duration, interpolator);

        viewAnimator.setOutAnimation(animation[0]);
        viewAnimator.setInAnimation(animation[1]);

        animation[0].setInterpolator(interpolator);
        animation[1].setInterpolator(interpolator);

        viewAnimator.setDisplayedChild(targetIndex);

        return animation[1];
    }

}
