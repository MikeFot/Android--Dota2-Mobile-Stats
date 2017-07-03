package com.michaelfotiadis.dota2viewer.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */

public final class ListUtils {

    private ListUtils() {
        // NOOP
    }

    public static <T> List<List<T>> chunk(final List<T> originalList, final int partitionSize) {

        final List<List<T>> partitions = new LinkedList<>();
        for (int i = 0; i < originalList.size(); i += partitionSize) {
            partitions.add(originalList.subList(i,
                    Math.min(i + partitionSize, originalList.size())));
        }

        return partitions;

    }


    /**
     * Converts a list to a typed list. Check for type first!
     *
     * @param list {@link List} to be converted
     * @param <T>  desired type
     * @return List of the desired type
     */
    public static <T> List<T> toTypedList(@NonNull final List<?> list) {
        //noinspection unchecked
        return (List<T>) list;
    }

    /**
     * Looks for the first non-null item in a list
     *
     * @param list list to be checked
     * @param <T>  type of the item
     * @return first non-null item
     */
    @Nullable
    public static <T> T getFirstNonNull(@NonNull final List<T> list) {

        for (final T item : list) {

            if (item != null) {
                return item;
            }

        }
        return null;
    }

}
