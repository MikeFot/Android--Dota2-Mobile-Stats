package com.michaelfotiadis.mobiledota2.ui.core.intent.dispatch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.michaelfotiadis.mobiledota2.ui.core.animation.AnimationBundleBuilder;
import com.michaelfotiadis.mobiledota2.ui.core.intent.NavLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;


/**
 *
 */
/*package*/ class IntentDispatcherInternal {

    private final Context mContext;
    private final AnimationBundleBuilder mAnimationHelper;

    private ActivityAnimation mAnimation;
    private boolean mForResult;
    private String mTransitionName;
    private Integer mRequestCode;
    private View mSourceView;

    public IntentDispatcherInternal(final Context context) {
        this.mAnimationHelper = new AnimationBundleBuilder(context);
        this.mContext = context;
    }

    public void dispatch(final Intent intent) {
        if (intent == null) {
            NavLog.w("Could not start Activity: Intent was null");
        } else {
            final Bundle theBundle = getBundle(mAnimation);
            try {
                if (mForResult) {
                    if (mContext instanceof Activity) {
                        ActivityCompat.startActivityForResult((Activity) mContext, intent, mRequestCode, theBundle);
                    } else {
                        NavLog.e("Cannot start activity for result as the context is not an activity");
                    }
                } else {
                    if (mContext instanceof Activity) {
                        ActivityCompat.startActivity(mContext, intent, theBundle);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mContext.startActivity(intent, theBundle);
                        } else {
                            mContext.startActivity(intent);
                        }
                    }
                }
            } catch (final ActivityNotFoundException e) {
                NavLog.w("Error starting activity", e);
            }
        }
        cleanUp();
    }

    private boolean canHandleTransition() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && TextUtils.isNotEmpty(mTransitionName)
                && mSourceView != null
                && mContext instanceof Activity;
    }

    private Bundle getBundle(final ActivityAnimation activityAnimation) {

        if (TextUtils.isNotEmpty(mTransitionName) && canHandleTransition()) {

            return mAnimationHelper.getTransitionBundle((Activity) mContext, mSourceView, mTransitionName);

        } else {

            if (activityAnimation == null) {
                return new Bundle();
            }

            switch (activityAnimation) {
                case SLIDE_IN_FROM_LEFT:
                    return mAnimationHelper.getSlideInFromRightBundle();
                case SCALE_UP_FROM_VIEW:
                    if (mSourceView != null) {
                        return mAnimationHelper.getScaleUpBundle(mSourceView);
                    }
                case SLIDE_UP_FROM_BOTTOM:
                    return mAnimationHelper.getSlideUpBundle();
                case ENTER_FROM_RIGHT:
                    return mAnimationHelper.getEnterFromRightBundle();
                default:
                    return new Bundle();
            }

        }

    }

    private void cleanUp() {
        this.mAnimation = null;
        this.mSourceView = null;
        this.mForResult = false;
        this.mRequestCode = Activity.RESULT_OK;
    }

    public IntentDispatcherInternal forResult(final int requestCode) {
        this.mForResult = true;
        this.mRequestCode = requestCode;
        return this;
    }

    public IntentDispatcherInternal withAnimation(final ActivityAnimation animation) {
        this.mAnimation = animation;
        return this;
    }

    public IntentDispatcherInternal withTransitionName(final String transitionName) {
        this.mTransitionName = transitionName;
        return this;
    }

    public IntentDispatcherInternal withView(final View sourceView) {
        this.mSourceView = sourceView;
        return this;
    }

    public void startService(Intent serviceIntent) {
        this.mContext.startService(serviceIntent);
    }

    public Context getContext() {
        return mContext;
    }
}
