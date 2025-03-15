package com.rperez.archpatterns

import com.rperez.archpatterns.repository.data.model.Stock
import com.rperez.archpatterns.repository.domain.repository.StockRepository
import com.rperez.archpatterns.repository.presentation.viewmodel.StockViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class StockViewModelTest {

    @get:Rule
    val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: StockViewModel
    private val mockRepository = mock<StockRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mock Stock History
        whenever(mockRepository.getStockHistory()).thenReturn(flowOf(emptyList()))

        viewModel = StockViewModel(mockRepository)
    }

    @Test
    fun `fetchStock should update stock state when successful`() = runTest {
        val testStock = Stock(symbol = "TSM", name = "TSMC", price = 120.0, timestamp = 1710657323)

        whenever(mockRepository.getStock("TSM")).thenReturn(testStock)

        viewModel.fetchStock("TSM")

        advanceUntilIdle()

        assertEquals(testStock, viewModel.stock.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `fetchStock should set error state when repository throws exception`() = runTest {
        whenever(mockRepository.getStock("TSM")).thenThrow(RuntimeException("API Error"))

        viewModel.fetchStock("TSM")

        advanceUntilIdle()

        assertNull(viewModel.stock.value)
        assertFalse(viewModel.isLoading.value)
        assertEquals("API Error", viewModel.error.value)
    }
}
