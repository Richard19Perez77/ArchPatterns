package com.rperez.archpatterns.mvi.model

sealed class StockIntent {
    data object Connect : StockIntent()
    data object Disconnect : StockIntent()
    data class SubscribeToSymbol(val symbol: String) : StockIntent()
}