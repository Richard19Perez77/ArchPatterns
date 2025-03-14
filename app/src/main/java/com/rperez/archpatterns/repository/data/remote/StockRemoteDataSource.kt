package com.rperez.archpatterns.repository.data.remote

import com.rperez.archpatterns.repository.data.model.Stock
import com.rperez.archpatterns.repository.data.remote.api.FinnhubApi
import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(
    private val finnhubApi: FinnhubApi,
    private val apiKey: String // Add API key as a dependency
) {

    suspend fun getStock(symbol: String): Stock {
        val response = finnhubApi.getStockQuote(symbol, apiKey)
        return Stock(
            symbol = symbol,
            name = "Taiwan Semiconductor Manufacturing Company",
            price = response.currentPrice,
            timestamp = response.timestamp
        )
    }
}