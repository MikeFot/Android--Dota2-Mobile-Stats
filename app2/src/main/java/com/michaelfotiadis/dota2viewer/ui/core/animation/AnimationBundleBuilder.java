package com.michaelfotiadis.dota2viewer.ui.core.animation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;


/**
 *
 */
public class AnimationBundleBuilder {

    private final Context mContext;

    public AnimationBundleBuilder(final Context context) {
        this.mContext = context;
    }

    public Bundle getCustomAnimationBundle(final int animationIn, final int animationOut) {
        final ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(mContext, animationIn, animationOut);
        return activityOptionsCompat.toBundle();
    }

    public Bundle getScaleUpBundle(final View view) {
        return getScaleUpBundle(view, view.getWidth(), view.getHeight());
    }

    @SuppressWarnings("MethodMayBeStatic")
    public Bundle getTransitionBundle(final Activity activity, final View view, final String transitionName) {
        final ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transitionName);
        return activityOptionsCompat.toBundle();
    }

    @SuppressWarnings("MethodMayBeStatic")
    public Bundle getScaleUpBundle(final View view, final int width, final int height) {
        final ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, width, height);
        return activityOptionsCompat.toBundle();
    }

    public Bundle getSlideUpBundle() {
        final ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.slide_up_bottom, R.anim.zoom_out);
        return activityOptionsCompat.toBundle();
    }

    public Bundle getEnterFromRightBundle() {
        return getCustomAnimationBundle(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    public Bundle getSlideInFromRightBundle() {
        return getCustomAnimationBundle(R.anim.slide_in_right, R.anim.zoom_out);
    }

    public Bundle getFadeInBundle() {
        return getCustomAnimationBundle(R.anim.fade_in, R.anim.fade_out);
    }


}
