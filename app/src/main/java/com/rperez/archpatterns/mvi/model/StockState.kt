package com.rperez.archpatterns.mvi.model

data class StockState(
    val isConnected: Boolean = false,
    val prices: List<Float> = emptyList(),
    val error: String? = null,
)