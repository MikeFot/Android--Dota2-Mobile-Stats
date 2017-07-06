package com.michaelfotiadis.dota2viewer.ui.core.animation.factory;

import java.io.File;

/**
 *
 */
/*package*/ final class AnimationFactoryUtils {

    private static final String DELIMITER = File.separator;
    private static final String EXTENSION = ".png";

    private AnimationFactoryUtils() {
        // NOOP
    }

    /*package*/
    static String buildFileName(final String path, final String fileNamePrefix, final int count) {
        return path +
                DELIMITER +
                fileNamePrefix +
                count +
                EXTENSION;
    }

    /*package*/
    static int calculateFrameDuration(final int frameCount, final long totalAnimationTime) {
        return (int) (totalAnimationTime / frameCount);
    }

}
