package com.rperez.archpatterns.repository.domain.repository

import com.rperez.archpatterns.repository.data.model.Stock

interface StockRepository {
    suspend fun getStock(symbol: String): Stock
}