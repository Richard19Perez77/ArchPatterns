package com.rperez.archpatterns.repository.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rperez.archpatterns.repository.data.model.Stock

@Composable
fun StockHistoryItem(stock: Stock) {

    val formattedTimestamp = stock.getFormattedTimestamp()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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