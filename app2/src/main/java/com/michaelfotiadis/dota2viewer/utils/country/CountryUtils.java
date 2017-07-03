package com.michaelfotiadis.dota2viewer.utils.country;

import java.util.Locale;

public final class CountryUtils {

    private CountryUtils() {
        // NOOP
    }

    public static String getCountryNameFromCountryCode(final String countryCode) {
        return countryCode == null ? "" : new Locale("", countryCode).getDisplayCountry();
    }

}
