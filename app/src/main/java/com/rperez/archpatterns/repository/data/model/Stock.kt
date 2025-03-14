package com.rperez.archpatterns.repository.data.model

data class Stock(
    val symbol: String,       // Stock symbol (e.g., "TSM")
    val name: String,        // Company name (e.g., "Taiwan Semiconductor Manufacturing Company")
    val price: Double,       // Current stock price
    val timestamp: Long      // Timestamp for caching
)