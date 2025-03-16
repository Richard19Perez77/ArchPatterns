package com.rperez.archpatterns

import com.rperez.archpatterns.repository.data.model.Stock
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.util.Date

class StockTest {

    @Test
    fun `getFormattedTimestamp returns correct formatted date`() {
        // Given: A Stock instance with a known timestamp (Unix time: 1700000000 -> GMT: 2023-11-14 10:13:20)
        val stock = Stock(symbol = "AAPL", name = "Apple Inc.", price = 150.0, timestamp = 1700000000L)

        // Expected formatted output (forcing UTC to ensure consistency in tests)
        val expectedFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val expectedDate = expectedFormat.format(Date(1700000000L * 1000)) // Convert to milliseconds

        // When: Calling getFormattedTimestamp()
        val actualFormattedDate = stock.getFormattedTimestamp()

        // Then: The formatted date should match the expected output
        assertEquals(expectedDate, actualFormattedDate)
    }

    @Test
    fun `getFormattedTimestamp returns default format for zero timestamp`() {
        val stock = Stock()
        var expectedDate = "1970-01-01 07:00:00"

        val actualFormattedDate = stock.getFormattedTimestamp()

        // Then: The formatted date should match the Unix Epoch start date
        assertEquals(expectedDate, actualFormattedDate)
    }
}
