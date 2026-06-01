/*
 * Copyright (c) 2011. Jay R. Gindin
 */
package hebrewcalendar

import kotlin.test.*
import kotlin.test.Test
import sternbach.software.kosherkotlin.hebrewcalendar.HebrewMonth
import sternbach.software.kosherkotlin.hebrewcalendar.JewishDate

/**
 *
 */
class UT_JewishDateNavigation {

    @Test
    fun jewishForwardMonthToMonth() {
        val jewishDate = JewishDate(5771, HebrewMonth.getMonthForValue(1), 1)
        assertEquals(5, jewishDate.gregorianLocalDate.dayOfMonth)
        assertEquals(3, jewishDate.gregorianLocalDate.monthNumber - 1)
        assertEquals(2011, jewishDate.gregorianLocalDate.year)
    }

    @Test
    fun computeRoshHashana5771() {

        // At one point, this test was failing as the JewishDate class spun through a never-ending loop...
        val jewishDate = JewishDate(5771, HebrewMonth.getMonthForValue(7), 1)
        assertEquals(9, jewishDate.gregorianLocalDate.dayOfMonth)
        assertEquals(8, jewishDate.gregorianLocalDate.monthNumber - 1)
        assertEquals(2010, jewishDate.gregorianLocalDate.year)
    }
} // End of UT_JewishDateNavigation class
