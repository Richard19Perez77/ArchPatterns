package com.rperez.archpatterns.repository.domain.repository

import com.rperez.archpatterns.repository.data.local.StockDatabase
import com.rperez.archpatterns.repository.data.local.entities.StockEntity
import com.rperez.archpatterns.repository.data.model.Stock
import com.rperez.archpatterns.repository.data.remote.StockRemoteDataSource
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val remoteDataSource: StockRemoteDataSource,
    private val localDataSource: StockDatabase
) : StockRepository {

    override suspend fun getStock(symbol: String): Stock {
        // Check local database first
        val localStock = localDataSource.stockDao().getStock(symbol)
        return if (localStock != null && isCacheValid(localStock.timestamp)) {
            // Return cached data if valid
            Stock(
                symbol = localStock.symbol,
                name = localStock.name,
                price = localStock.price,
                timestamp = localStock.timestamp
            )
        } else {
            // Fetch from remote API
            val remoteStock = remoteDataSource.getStock(symbol)
            // Cache the data locally
            localDataSource.stockDao().insertStock(
                StockEntity(
                    symbol = remoteStock.symbol,
                    name = remoteStock.name,
                    price = remoteStock.price,
                    timestamp = remoteStock.timestamp
                )
            )
            remoteStock
        }
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val cacheDuration = 5 * 60 * 1000 // 5 minutes cache validity
        return (currentTime - timestamp) < cacheDuration
    }
}