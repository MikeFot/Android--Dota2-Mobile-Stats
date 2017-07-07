package com.michaelfotiadis.mobiledota2.utils.dota;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

public class DotaTimeUtils {

    private DotaTimeUtils() {
    }

    public static String getTimeAgoForMatch(@NonNull final Resources resources,
                                            final long startTime,
                                            @Nullable final Integer duration) {

        final DateTime now = new DateTime(System.currentTimeMillis(), DateTimeZone.UTC);

        final long endTime = duration == null ? startTime : startTime + duration;

        final DateTime matchEnd = new DateTime(endTime * 1000, DateTimeZone.UTC);

        if (now.isBefore(matchEnd)) {
            return ("Just now");
        }

        final Interval interval = new Interval(matchEnd, now);
        if (interval.toPeriod().getYears() > 1) {
            return (interval.toPeriod().getYears()) + " years ago";
        }
        if (interval.toPeriod().getYears() == 1) {
            return (interval.toPeriod().getYears()) + " year ago";
        }
        if (interval.toPeriod().getMonths() > 1) {
            return (interval.toPeriod().getMonths()) + " months ago";
        }
        if (interval.toPeriod().getMonths() == 1) {
            return (interval.toPeriod().getMonths()) + " month ago";
        }
        if (interval.toPeriod().getWeeks() > 1) {
            return (interval.toPeriod().getWeeks()) + " weeks ago";
        }
        if (interval.toPeriod().getWeeks() == 1) {
            return (interval.toPeriod().getWeeks()) + " week ago";
        }
        if (interval.toPeriod().getDays() > 1) {
            return (interval.toPeriod().getDays()) + " days ago";
        }
        if (interval.toPeriod().getDays() == 1) {
            return (interval.toPeriod().getDays()) + " day ago";
        }
        if (interval.toPeriod().getHours() > 1) {
            return (interval.toPeriod().getHours()) + " hours ago";
        }
        if (interval.toPeriod().getHours() == 1) {
            return (interval.toPeriod().getHours()) + " hour ago";
        }
        if (interval.toPeriod().getMinutes() > 1) {
            return (interval.toPeriod().getMinutes()) + " minutes ago";
        }
        return ("Just now");
    }

}
