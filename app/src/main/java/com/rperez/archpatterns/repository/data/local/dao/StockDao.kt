package com.rperez.archpatterns.repository.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rperez.archpatterns.repository.data.local.entities.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)

    @Query("DELETE FROM stocks WHERE symbol = :symbol")
    suspend fun deleteStock(symbol: String)

    @Query("SELECT * FROM stocks WHERE symbol = :symbol ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestStock(symbol: String): StockEntity?

    @Query("SELECT * FROM stocks ORDER BY timestamp DESC")
    fun getStockHistory(): Flow<List<StockEntity>>
}