package com.rperez.archpatterns.repository.di

import android.content.Context
import com.rperez.archpatterns.repository.data.local.StockDatabase
import com.rperez.archpatterns.repository.data.remote.StockRemoteDataSource
import com.rperez.archpatterns.repository.data.remote.api.FinnhubApi
import com.rperez.archpatterns.repository.domain.repository.StockRepository
import com.rperez.archpatterns.repository.data.repository.StockRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return StockDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFinnhubApi(): FinnhubApi {
        return Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinnhubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockRemoteDataSource(
        finnhubApi: FinnhubApi,
        apiKey: String // Add API key as a dependency
    ): StockRemoteDataSource {
        return StockRemoteDataSource(finnhubApi, apiKey)
    }

    @Provides
    @Singleton
    fun provideStockRepository(
        remoteDataSource: StockRemoteDataSource,
        localDataSource: StockDatabase
    ): StockRepository {
        return StockRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return "cva0fg1r01qpd9s9vosgcva0fg1r01qpd9s9vot0" // Replace with your actual API key
    }
}