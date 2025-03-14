package com.rperez.archpatterns.repository.data.remote.api

import com.rperez.archpatterns.repository.data.remote.models.FinnhubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {

    @GET("quote")
    suspend fun getStockQuote(
        @Query("symbol") symbol: String,
        @Query("token") apiKey: String
    ): FinnhubResponse
}