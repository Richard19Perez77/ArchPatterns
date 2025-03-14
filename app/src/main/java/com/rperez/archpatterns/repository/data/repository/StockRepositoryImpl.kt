package com.rperez.archpatterns.repository.data.repository

import com.rperez.archpatterns.repository.data.local.StockDatabase
import com.rperez.archpatterns.repository.data.local.entities.StockEntity
import com.rperez.archpatterns.repository.data.model.Stock
import com.rperez.archpatterns.repository.data.remote.StockRemoteDataSource
import com.rperez.archpatterns.repository.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val remoteDataSource: StockRemoteDataSource,
    private val localDataSource: StockDatabase
) : StockRepository {

    override suspend fun getStock(symbol: String): Stock {
        // Check local database first
        val localStock = localDataSource.stockDao().getLatestStock(symbol)
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

    override fun getStockHistory(): Flow<List<Stock>> {
        return localDataSource.stockDao().getStockHistory()
            .map { stockEntities ->
                stockEntities.map { entity ->
                    Stock(
                        symbol = entity.symbol,
                        name = entity.name,
                        price = entity.price,
                        timestamp = entity.timestamp
                    )
                }
            }
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val cacheDuration = 60 * 5 // dealing with seconds 60 1 minute * 5 for 5 minutes
        val currentCacheTime = currentTime - timestamp
        return currentCacheTime < cacheDuration
    }
}