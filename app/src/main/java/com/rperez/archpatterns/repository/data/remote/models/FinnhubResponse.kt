package com.rperez.archpatterns.repository.data.remote.models

import com.google.gson.annotations.SerializedName

data class FinnhubResponse(
    @SerializedName("c") val currentPrice: Double = 0.0,
    @SerializedName("d") val change: Double = 0.0,
    @SerializedName("dp") val percentChange: Double = 0.0,
    @SerializedName("h") val high: Double = 0.0,
    @SerializedName("l") val low: Double = 0.0,
    @SerializedName("o") val open: Double = 0.0,
    @SerializedName("pc") val previousClose: Double = 0.0,
    @SerializedName("t") val timestamp: Long = 0L
)
