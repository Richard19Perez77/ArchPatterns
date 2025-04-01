package com.rperez.archpatterns.mvi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rperez.archpatterns.mvi.model.StockIntent
import com.rperez.archpatterns.mvi.viewmodel.StockViewModel

@Composable
fun StockLiveScreen(vm: StockViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (state.isConnected) {
            Text("Connected", color = Color(0xFF006400))
        } else {
            Button(onClick = { vm.process(StockIntent.Connect) }) {
                Text("Connect")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            vm.process(StockIntent.SubscribeToSymbol("TSM"))
        }) {
            Text("Subscribe to TSM")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "TSMC: ${state.prices.lastOrNull() ?: "Loading..."}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(state.prices.reversed()) { price ->
                Text("TSM: $price")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: $it", color = Color.Red)
        }
    }
}