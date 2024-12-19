package com.droidcon.voices.ui.voices

import com.droidcon.voices.ui.util.Util
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilTest {

    /**
     * Test the functionality of [Util.msToTimeStr]
     * For 3 different time values
     */
    @Test
    fun util_msToStr_WorksCorrectly() {
        val seconds1 : Long = 2 * 3600 + 3 * 60 + 23
        val ms1 = seconds1 * 1000
        val answer1 = "2h3m23s"
        assertEquals(Util.msToTimeStr(ms1), answer1)

        val seconds2 : Long = 3 * 60 + 31
        val ms2 = seconds2 * 1000
        val answer2 = "3m31s"
        assertEquals(Util.msToTimeStr(ms2), answer2)

        val seconds3 : Long = 24
        val ms3 = seconds3 * 1000
        val answer3 = "24s"
        assertEquals(Util.msToTimeStr(ms3), answer3)
    }
}