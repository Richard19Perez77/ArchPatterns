package com.rperez.archpatterns.repository.domain.repository

import com.rperez.archpatterns.repository.data.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getStock(symbol: String): Stock
    fun getStockHistory(): Flow<List<Stock>> // Add stock history retrieval
}