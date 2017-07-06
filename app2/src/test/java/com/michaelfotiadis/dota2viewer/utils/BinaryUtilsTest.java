package com.michaelfotiadis.dota2viewer.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BinaryUtilsTest {
    @Test
    public void checkBitStatus() throws Exception {
        assertTrue(BinaryUtils.checkBitStatus(63, 0));
        assertTrue(BinaryUtils.checkBitStatus(63, 1));
        assertTrue(BinaryUtils.checkBitStatus(63, 2));
        assertTrue(BinaryUtils.checkBitStatus(63, 3));
        assertTrue(BinaryUtils.checkBitStatus(63, 4));
        assertTrue(BinaryUtils.checkBitStatus(63, 5));
        assertFalse(BinaryUtils.checkBitStatus(63, 6));
    }

}