package com.rperez.archpatterns.repository.data.remote.models

import com.google.gson.annotations.SerializedName

data class FinnhubResponse(
    @SerializedName("c") val currentPrice: Double,  // Current price
    @SerializedName("d") val change: Double,        // Change in price
    @SerializedName("dp") val percentChange: Double, // Percent change
    @SerializedName("h") val high: Double,          // High price of the day
    @SerializedName("l") val low: Double,           // Low price of the day
    @SerializedName("o") val open: Double,          // Open price of the day
    @SerializedName("pc") val previousClose: Double, // Previous close price
    @SerializedName("t") val timestamp: Long        // Timestamp
)