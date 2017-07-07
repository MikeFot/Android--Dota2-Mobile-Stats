package com.michaelfotiadis.mobiledota2.ui.view.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;


/**
 *
 */
public final class ViewUtils {
    private ViewUtils() {
        // NOOP
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(final float dp, final Context context) {
        final Resources resources = context.getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(final float px, final Context context) {
        final Resources resources = context.getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static boolean isEllipsized(final TextView tv) {
        final Layout layout = tv.getLayout();
        final int lines = layout.getLineCount();
        if (lines > 0) {
            final int ellipsisCount = layout.getEllipsisCount(lines - 1);
            return ellipsisCount > 0;
        } else {
            return false;
        }
    }

    /**
     * Will try to set the text in a {@link TextView} if the passed text is not empty.
     * If the {@param text} is empty or null (as asserted by {@link TextUtils#isEmpty(CharSequence)}),
     * it will set the visibility of the TextView to {@link View#GONE}.
     * If {@param text} is not empty it will set the visibility of the TextView to {@link View#VISIBLE}.
     *
     * @param view the {@link TextView}
     * @param text the {@link CharSequence} we want to display
     * @return true if the TextView is visible after the operation, false otherwise.
     */
    public static boolean setTextOrHide(final TextView view, final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(View.GONE);
            view.setText(null);
            return false;
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
            return true;
        }
    }

    public static void setViewHeight(final View view, final int heightInPixels) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = heightInPixels;
        view.setLayoutParams(params);
    }

    public static void setViewWidth(final View view, final int widthInPixels) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = widthInPixels;
        view.setLayoutParams(params);
    }

    public static void showView(final View view, final boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void showViewAnimated(final View view, final boolean show) {
        if (show) {
            ViewAnimator.expand(view);
        } else {
            ViewAnimator.collapse(view);
        }
    }

    public static void showViewAnimated(final View view, final boolean show, final int speedFactor) {
        if (show) {
            ViewAnimator.expand(view, speedFactor);
        } else {
            ViewAnimator.collapse(view, speedFactor);
        }
    }

    public static void addCompoundDrawableLeft(final TextView textView, final Drawable drawable) {

        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

    }

    public static void addCompoundDrawableLeft(final TextView textView,
                                               final Drawable drawable,
                                               final int width,
                                               final int height) {


        drawable.setBounds(0, 0, width, height);
        textView.setCompoundDrawables(drawable, null, null, null);

    }

    public static void slideFromRight(final View view) {
        ViewAnimator.slideFromRight(view);
    }

    public static void slideUpView(final View view) {
        ViewAnimator.slideUpFromBottom(view);
    }

    public static void slideOutView(final View view) {
        ViewAnimator.slideOut(view);
    }

    /**
     * Gets the collapse progress of the app bar layout
     *
     * @param appBarLayout   app bar layout
     * @param verticalOffset integer vertical offset from an offset changed event
     * @return 100 if fully collapsed, 0 if fully expanded
     */
    public static int getAppBarCollapsePercentage(final AppBarLayout appBarLayout, final int verticalOffset) {
        final int totalScrollRange = appBarLayout.getTotalScrollRange();
        final int currentScrollPercentage;
        //noinspection IfMayBeConditional
        if (totalScrollRange == 0) {
            currentScrollPercentage = 100;
        } else {
            currentScrollPercentage = (Math.abs(verticalOffset)) * 100 / appBarLayout.getTotalScrollRange();
        }
        return currentScrollPercentage;
    }

    public static void setDrawable(final View view, final int drawableResId) {
        setDrawable(view, ContextCompat.getDrawable(view.getContext(), drawableResId));
    }

    public static Drawable tintDrawable(final Drawable drawable, @ColorInt final int tintColor) {

        final Drawable modifiedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(modifiedDrawable.mutate(), tintColor);
        return modifiedDrawable;

    }

    public static void setDrawable(final View view, final Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setDrawableLeft(final TextView textView,
                                       final Drawable drawable,
                                       final int tintColor) {
        final Drawable tintedDrawable = ViewUtils.tintDrawable(drawable, tintColor);
        textView.setCompoundDrawablePadding((int) textView.getContext().getResources().getDimension(R.dimen.padding_4dp));
        textView.setCompoundDrawablesWithIntrinsicBounds(tintedDrawable, null, null, null);
    }

    public static void setDrawableLeft(final TextView textView,
                                       final Drawable drawable) {
        textView.setCompoundDrawablePadding((int) textView.getContext().getResources().getDimension(R.dimen.padding_4dp));
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    public static void setDrawableRight(final TextView textView,
                                        final Drawable drawable,
                                        final int tintColor) {

        final Drawable tintedDrawable = ViewUtils.tintDrawable(drawable, tintColor);
        setDrawableRight(textView, tintedDrawable);
    }

    public static void setDrawableRight(final TextView textView,
                                        final Drawable drawable) {

        textView.setCompoundDrawablePadding((int) textView.getContext().getResources().getDimension(R.dimen.padding_4dp));
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

}
