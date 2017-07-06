package com.michaelfotiadis.dota2viewer.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ListUtilsTest {
    @Test
    public void chunk() throws Exception {

        final List<Object> list = new ArrayList<>();

        final int size = 750;
        final int chunkSize = 100;
        final int expectedSize = size / chunkSize + 1;

        for (int i = 0; i < size; i++) {

            list.add(new Object());

        }

        assertEquals(size, list.size());

        final List<List<Object>> partitions = ListUtils.chunk(list, chunkSize);

        assertEquals(expectedSize, partitions.size());

        assertEquals(chunkSize, partitions.get(0).size());

        // yay, I got to use modulo!
        assertEquals(size % chunkSize, partitions.get(partitions.size() - 1).size());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void toTypedList() throws Exception {

        final List list = new ArrayList<>();
        list.add("bob");
        list.add("Mary");

        final List<String> typedList = ListUtils.toTypedList(list);

        assertNotNull(typedList);
        assertTrue(typedList.get(0).getClass().isAssignableFrom(String.class));
        assertEquals("bob", typedList.get(0));

    }

    @Test
    public void getFirstNonNull() throws Exception {

        final String nonNull = "mid or feed";

        final List<String> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(nonNull);
        list.add(null);
        list.add("first pick techies");

        final String firstNonNull = ListUtils.getFirstNonNull(list);
        assertEquals(nonNull, firstNonNull);


    }
}