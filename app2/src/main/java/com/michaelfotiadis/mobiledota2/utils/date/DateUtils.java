package com.michaelfotiadis.mobiledota2.utils.date;

public class DateUtils {

    public static String getDateFromUnixTime(final long millis, final String dateFormat) {
        return new UtcDateFormatter(dateFormat).format(millis);
    }

}
