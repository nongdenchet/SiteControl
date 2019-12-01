package com.rain.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/*
 * Created by quan.vu on 10/6/18.
 */
class EncryptorTest {

    @Test
    fun hashValueWithSHA256() {
        assertEquals("35072c1a546350e0ba7a11d49c6f12e72c57e7b671225b1781", hash256("hello_world"))
        assertEquals("5e8488a28047151056fd6292773603d0d6abd62a11f721d15428", hash256("password"))
        assertEquals("f60184c7d65a2fa055a0153f4f1b2b0b22c15d6c15000a08", hash256("test"))
    }
}
