package com.rperez.archpatterns.repository.data.local.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "stocks",
    primaryKeys = ["symbol", "timestamp"], // Composite primary key
    indices = [Index(value = ["symbol"])]  // Index for faster symbol queries
)
data class StockEntity(
    val symbol: String,  // Now part of the primary key
    val timestamp: Long, // Also part of the primary key (stores unique records)
    val name: String,
    val price: Double
)
