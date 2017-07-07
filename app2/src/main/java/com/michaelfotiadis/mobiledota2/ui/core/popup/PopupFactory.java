package com.michaelfotiadis.mobiledota2.ui.core.popup;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class PopupFactory {

    private static final float DEFAULT_PADDING = 0f;
    private static final boolean IS_ANIMATED = true;
    private static final long ANIMATION_DURATION = 3000L;
    private static final boolean IS_TRANSPARENT = true;

    private final Context mContext;

    public PopupFactory(final Context context) {
        mContext = context;
    }

    public SimpleTooltip.Builder getDefault() {

        return new SimpleTooltip.Builder(mContext)
                .padding(DEFAULT_PADDING)
                .animated(IS_ANIMATED)
                .animationDuration(ANIMATION_DURATION)
                .transparentOverlay(IS_TRANSPARENT);

    }

    public SimpleTooltip.Builder get(@NonNull final ImageView imageView,
                                     @ColorInt final int startColor,
                                     @ColorInt final int endColor) {
        return getDefault()
                .anchorView(imageView)
                .arrowColor(endColor)
                .onDismissListener(new SimpleTooltip.OnDismissListener() {
                    @Override
                    public void onDismiss(final SimpleTooltip tooltip) {
                        // this is to avoid onConfigurationChanged issues
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setColorFilter(startColor);
                            }
                        });
                    }
                });
    }

    public SimpleTooltip.Builder get(@NonNull final ImageView imageView,
                                     @ColorInt final int arrowColor) {
        return getDefault()
                .anchorView(imageView)
                .arrowColor(arrowColor);
    }


}
