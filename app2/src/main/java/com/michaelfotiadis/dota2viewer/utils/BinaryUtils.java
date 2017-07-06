package com.michaelfotiadis.dota2viewer.utils;

/**
 * Created by Map on 07/06/2015.
 */
public class BinaryUtils {

    public static boolean checkBitStatus(final long value, final int bitPosition) {
        return ((value & (1L << bitPosition)) != 0);
    }

}
