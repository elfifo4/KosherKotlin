package hebrewcalendar

import sternbach.software.kosherkotlin.hebrewcalendar.HebrewDateFormatter
import sternbach.software.kosherkotlin.hebrewcalendar.HebrewMonth
import sternbach.software.kosherkotlin.hebrewcalendar.JewishCalendar
import sternbach.software.kosherkotlin.hebrewcalendar.YomiCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class YomiCalculatorTest {
    private val calc = YomiCalculator
    @Test
    fun testCorrectDaf1() {
        val jewishCalendar = JewishCalendar(5685, HebrewMonth.KISLEV, 12)
        val daf = calc.getDafYomiBavli(jewishCalendar)
        assertEquals(5, daf!!.masechtaNumber)
        assertEquals(2, daf.daf)
        println(hdf.formatDafYomiBavli(jewishCalendar.dafYomiBavli!!))
    }

    @Test
    fun testCorrectDaf2() {
        val jewishCalendar = JewishCalendar(5736, HebrewMonth.ELUL, 26)
        val daf = calc.getDafYomiBavli(jewishCalendar)
        assertEquals(4, daf!!.masechtaNumber)
        assertEquals(14, daf.daf)
        println(hdf.formatDafYomiBavli(jewishCalendar.dafYomiBavli!!))
    }

    @Test
    fun testCorrectDaf3() {
        val jewishCalendar = JewishCalendar(5777, HebrewMonth.ELUL, 10)
        val daf = calc.getDafYomiBavli(jewishCalendar)
        assertEquals(23, daf!!.masechtaNumber)
        assertEquals(47, daf.daf)
        println(hdf.formatDafYomiBavli(jewishCalendar.dafYomiBavli!!))
    }

    companion object {
        private val hdf = HebrewDateFormatter()

        init {
            hdf.isHebrewFormat = true
        }
    }
}
