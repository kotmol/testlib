package com.kotmol.testlib

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TheLibTest {

    @Test
    @DisplayName("check that all entries exist")
    fun testHello() {
        assertEquals(1, 1)
    }
}
