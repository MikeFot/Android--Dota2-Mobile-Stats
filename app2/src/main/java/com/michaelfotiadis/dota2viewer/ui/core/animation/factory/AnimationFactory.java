package com.michaelfotiadis.dota2viewer.ui.core.animation.factory;

import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.michaelfotiadis.dota2viewer.utils.AppLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AnimationFactory {
    private final AssetManager mAssetManager;

    public AnimationFactory(final AssetManager assetManager) {
        mAssetManager = assetManager;
    }

    public AnimationDrawable create(final AnimationFactoryInput input) {
        return makeAnimation(input.getPath(), input.getFileNamePrefix(), input.getFrameCount(), input.getTotalAnimationTime());
    }

    /**
     * @param path               {@link String} path of the assets folder without file separator e.g. "images"
     * @param fileNamePrefix     {@link String} file name prefix e.g. "image_"
     * @param frameCount         integer number of images to load
     * @param totalAnimationTime long total desired time of the animation
     * @return generated {@link AnimationDrawable}
     */
    private AnimationDrawable makeAnimation(final String path,
                                            final String fileNamePrefix,
                                            final int frameCount,
                                            final long totalAnimationTime) {

        final AnimationDrawable animationDrawable = new AnimationDrawable();

        final int frameDuration = AnimationFactoryUtils.calculateFrameDuration(frameCount, totalAnimationTime);

        final List<Drawable> drawables = loadDrawables(path, fileNamePrefix, frameCount);

        for (final Drawable drawable : drawables) {
            animationDrawable.addFrame(drawable, frameDuration);
        }
        animationDrawable.setOneShot(false);
        return animationDrawable;

    }

    /**
     * @param path           {@link String} path of the assets folder without file separator e.g. "images"
     * @param fileNamePrefix {@link String} file name prefix e.g. "image_"
     * @param frameCount     integer number of images to load
     * @return List of drawables loaded
     */
    private List<Drawable> loadDrawables(final String path,
                                         final String fileNamePrefix,
                                         final int frameCount) {


        final List<Drawable> drawables = new ArrayList<>();

        for (int i = 0; i < frameCount; i++) {
            final String fileName = AnimationFactoryUtils.buildFileName(path, fileNamePrefix, i);
            AppLog.d("Fetching image: " + fileName);
            try {
                drawables.add(Drawable.createFromStream(mAssetManager.open(fileName), null));
            } catch (final IOException e) {
                AppLog.e("Image Fetching: IOException: " + e.getMessage());
            }
        }
        return drawables;
    }


}
