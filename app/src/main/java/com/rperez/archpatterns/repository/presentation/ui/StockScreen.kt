package com.rperez.archpatterns.repository.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rperez.archpatterns.repository.presentation.viewmodel.StockViewModel
import com.rperez.archpatterns.repository.data.model.Stock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StockScreen(viewModel: StockViewModel = hiltViewModel()) {
    val stock by viewModel.stock.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val stockHistory by viewModel.stockHistory.collectAsState()

    // Fetch stock data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchStock("TSM") // Fetch TSMC stock data
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp
            )
        } else if (stock != null) {
            stock?.let { StockDetails(it) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show history list if available
        if (stockHistory.isNotEmpty()) {
            Text(
                text = "Stock History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                items(stockHistory) { stockItem ->
                    StockHistoryItem(stockItem)
                }
            }
        }
    }
}

@Composable
fun StockDetails(stock: Stock) {

    fun formatTimestamp(timestamp: Long): String {
        return try {
            val date = Date(timestamp * 1000)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.format(date)
        } catch (_: Exception) {
            "Invalid Date"
        }
    }

    val formattedTimestamp = formatTimestamp(stock.timestamp) // Convert timestamp to date

    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stock.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Symbol: ${stock.symbol}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,

            text = "Price: $${stock.price}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Updated: $formattedTimestamp",
            fontSize = 18.sp
        )
    }
}

@Composable
fun StockHistoryItem(stock: Stock) {

    fun formatTimestamp(timestamp: Long): String {
        return try {
            val date = Date(timestamp * 1000)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.format(date)
        } catch (_: Exception) {
            "Invalid Date"
        }
    }

    val formattedTimestamp = formatTimestamp(stock.timestamp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stock.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Symbol: ${stock.symbol}", fontSize = 16.sp)
            Text(text = "Price: $${stock.price}", fontSize = 16.sp)
            Text(
                text = "Updated: $formattedTimestamp",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}