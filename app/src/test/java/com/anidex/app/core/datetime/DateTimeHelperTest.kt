package com.anidex.app.core.datetime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeHelperTest {

    @Test
    fun `parseDayOfWeek handles plurals and casing accurately`() {
        assertEquals(DayOfWeek.SUNDAY, DateTimeHelper.parseDayOfWeek("Sundays"))
        assertEquals(DayOfWeek.MONDAY, DateTimeHelper.parseDayOfWeek("monday"))
        assertEquals(DayOfWeek.FRIDAY, DateTimeHelper.parseDayOfWeek("Fridays"))
    }

    @Test
    fun `calculateLocalAirTime correctly rolls day backwards when converting JST to WIB`() {
        // Tokyo Sunday 00:30 (UTC+9) should be Jakarta Saturday 22:30 (UTC+7, WIB)
        val jstZone = ZoneId.of("Asia/Tokyo")
        val wibZone = ZoneId.of("Asia/Jakarta")

        // Fixed reference time: 2026-10-18 Sunday 00:00 JST
        val referenceTime = ZonedDateTime.of(2026, 10, 18, 0, 0, 0, 0, jstZone)

        val localTime = DateTimeHelper.calculateLocalAirTime(
            broadcastDay = "Sundays",
            broadcastTimeJst = "00:30",
            referenceTime = referenceTime,
            targetZoneId = wibZone
        )

        assertNotNull(localTime)
        assertEquals(DayOfWeek.SATURDAY, localTime!!.dayOfWeek)
        assertEquals(22, localTime.hour)
        assertEquals(30, localTime.minute)
    }
}

