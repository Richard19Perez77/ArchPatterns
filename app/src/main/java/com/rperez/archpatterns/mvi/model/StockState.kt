package com.rperez.archpatterns.mvi.model

data class StockState(
    val isConnected: Boolean = false,
    val prices: Map<String, Float> = emptyMap(),
    val error: String? = null
)