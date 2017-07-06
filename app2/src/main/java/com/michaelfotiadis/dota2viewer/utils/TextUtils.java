package com.michaelfotiadis.dota2viewer.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 */

public final class TextUtils {

    private TextUtils() {
        // NOOP
    }

    /**
     * Copy of the {@link android.text.TextUtils} method, as this can be mocked
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable final CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(@Nullable final CharSequence str) {
        return !isEmpty(str);
    }

    public static String padLeftWithZeroes(@NonNull final String content,
                                           final int digits) {
        return padLeftWithChar(content, '0', digits);
    }

    public static String padLeftWithChar(@NonNull final String content,
                                         char chr,
                                         final int digits) {
        String zeroes = "";
        String chrAsString = String.valueOf(chr);
        int j = 0;
        while (j < digits) {
            zeroes += chrAsString;
            j++;
        }
        return (zeroes + content).substring(content.length());
    }

    public static String cropLeft(@NonNull final String content,
                                  final int expectedLength) {
        return content.substring(0, Math.min(expectedLength, content.length()));
    }

}
