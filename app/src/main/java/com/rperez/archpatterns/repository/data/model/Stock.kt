package com.rperez.archpatterns.repository.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Stock(
    val symbol: String = "N/A",
    val name: String = "Unknown Company",
    val price: Double = 0.0,
    val timestamp: Long = 0L
) {
    fun getFormattedTimestamp(): String {
        val date = Date(timestamp * 1000) // Convert Unix timestamp to milliseconds
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}