package com.drwho174.calc1.ui.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FormatsTest {

    @Test
    fun `comma decimal separator is accepted`() {
        assertEquals(1.97, "1,97".toNumberOrNull()!!, 1e-9)
    }

    @Test
    fun `dot decimal separator is accepted`() {
        assertEquals(1.97, "1.97".toNumberOrNull()!!, 1e-9)
    }

    @Test
    fun `surrounding spaces are ignored`() {
        assertEquals(70.0, " 70 ".toNumberOrNull()!!, 1e-9)
    }

    @Test
    fun `non numeric input returns null`() {
        assertNull("".toNumberOrNull())
        assertNull("abc".toNumberOrNull())
        assertNull("1,2,3".toNumberOrNull())
    }
}
