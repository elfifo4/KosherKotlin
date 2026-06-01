package hebrewcalendar

import kotlinx.datetime.LocalDate
import sternbach.software.kosherkotlin.hebrewcalendar.HebrewDateFormatter
import sternbach.software.kosherkotlin.hebrewcalendar.HebrewMonth
import sternbach.software.kosherkotlin.hebrewcalendar.JewishCalendar
import sternbach.software.kosherkotlin.hebrewcalendar.JewishDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JewishCalendarParshaRegressionTest {

    @Test
    fun `18 Nissan 5786 should return Shmini`() {
        val calendar = JewishCalendar(
            LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 5),
        )

        assertEquals(
            JewishCalendar.Parsha.SHMINI,
            calendar.upcomingParshah,
        )
    }

    @Test
    fun `one week later should return Tazria Metzora`() {
        val calendar = JewishCalendar(
            LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 12),
        )

        assertEquals(
            JewishCalendar.Parsha.TAZRIA_METZORA,
            calendar.upcomingParshah,
        )
    }

    @Test
    fun `formatParsha should match Shmini on 18 Nissan 5786`() {
        val calendar = JewishCalendar(
            LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 11),
        )
        val formatter = HebrewDateFormatter()

        assertEquals(
            "Shmini",
            formatter.formatParsha(calendar),
        )
    }

    @Test
    fun `formatParsha should match Tazria Metzora one week later`() {
        val calendar = JewishCalendar(
            LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 18),
        )
        val formatter = HebrewDateFormatter()

        assertEquals(
            "Tazria Metzora",
            formatter.formatParsha(calendar),
        )
    }

    @Test
    fun `getMonthForValue should wrap correctly around boundaries`() {
        assertEquals(HebrewMonth.NISSAN, HebrewMonth.getMonthForValue(1))
        assertEquals(HebrewMonth.ADAR_II, HebrewMonth.getMonthForValue(13))
        assertEquals(HebrewMonth.ADAR_II, HebrewMonth.getMonthForValue(0))
        assertEquals(HebrewMonth.ADAR, HebrewMonth.getMonthForValue(-1))
        assertEquals(HebrewMonth.NISSAN, HebrewMonth.getMonthForValue(14))
        assertEquals(HebrewMonth.IYAR, HebrewMonth.getMonthForValue(15))
    }

    @Test
    fun `Nissan previousMonth should be Adar II`() {
        assertEquals(
            HebrewMonth.ADAR_II,
            HebrewMonth.NISSAN.previousMonth,
        )
    }

    @Test
    fun `daysSinceStartOfJewishYear should increase from 24 Nissan to 1 Iyar`() {
        val nissan24 = JewishDate(
            hebrewYear = 5786,
            hebrewMonth = HebrewMonth.NISSAN,
            hebrewDayOfMonth = 24,
        )
        val iyar1 = JewishDate(
            hebrewYear = 5786,
            hebrewMonth = HebrewMonth.IYAR,
            hebrewDayOfMonth = 1,
        )

        assertTrue(
            nissan24.daysSinceStartOfJewishYear < iyar1.daysSinceStartOfJewishYear,
            "Expected 24 Nissan to be before 1 Iyar within the same Jewish year",
        )
    }

    @Test
    fun `daysSinceStartOfJewishYear should increase day by day within Nissan`() {
        val day18 = JewishDate(
            hebrewYear = 5786,
            hebrewMonth = HebrewMonth.NISSAN,
            hebrewDayOfMonth = 18,
        )
        val day19 = JewishDate(
            hebrewYear = 5786,
            hebrewMonth = HebrewMonth.NISSAN,
            hebrewDayOfMonth = 19,
        )

        assertEquals(
            day18.daysSinceStartOfJewishYear + 1,
            day19.daysSinceStartOfJewishYear,
        )
    }

    @Test
    fun `upcomingParshah should remain stable throughout the same week before Shabbos`() {
        val sunday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 5))
        val monday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 6))
        val tuesday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 7))
        val wednesday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 8))
        val thursday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 9))
        val friday = JewishCalendar(LocalDate(year = 2026, monthNumber = 4, dayOfMonth = 10))

        assertEquals(JewishCalendar.Parsha.SHMINI, sunday.upcomingParshah)
        assertEquals(JewishCalendar.Parsha.SHMINI, monday.upcomingParshah)
        assertEquals(JewishCalendar.Parsha.SHMINI, tuesday.upcomingParshah)
        assertEquals(JewishCalendar.Parsha.SHMINI, wednesday.upcomingParshah)
        assertEquals(JewishCalendar.Parsha.SHMINI, thursday.upcomingParshah)
        assertEquals(JewishCalendar.Parsha.SHMINI, friday.upcomingParshah)
    }
}
