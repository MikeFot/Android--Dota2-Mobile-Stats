package com.michaelfotiadis.mobiledota2.utils.format;

import java.text.DecimalFormat;

/**
 *
 */
public final class FormattingUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");
    private static final String TEXT_SPECIAL_CHARS = "[^\\w]";

    private FormattingUtils() {
    }


    public static String formatStringToLowerCaseNoSpecial(final String text) {
        return text.replaceAll(TEXT_SPECIAL_CHARS, "_").toLowerCase();
    }

    public static String formatTwoDigitString(final int number) {
        if (0 == number) {
            return "00";
        }

        if (0 == number / 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public static String formatThousandsNumberString(final int number) {
        return DECIMAL_FORMAT.format(number / 1000f) + "k";
    }
}
