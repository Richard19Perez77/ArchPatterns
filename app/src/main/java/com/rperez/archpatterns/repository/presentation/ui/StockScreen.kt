package com.rperez.archpatterns.repository.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rperez.archpatterns.repository.presentation.viewmodel.StockViewModel
import com.rperez.archpatterns.repository.data.model.Stock

@Composable
fun StockScreen(viewModel: StockViewModel = hiltViewModel()) {
    val stock by viewModel.stock.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(
                text = error!!, color = MaterialTheme.colorScheme.error, fontSize = 16.sp
            )
        } else if (stock != null) {
            StockDetails(stock!!)
        }
    }

    // Fetch stock data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchStock("TSM") // Fetch TSMC stock data
    }
}

@Composable
fun StockDetails(stock: Stock) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stock.name, fontSize = 24.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Symbol: ${stock.symbol}", fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Price: $${stock.price}", fontSize = 18.sp
        )
    }
}