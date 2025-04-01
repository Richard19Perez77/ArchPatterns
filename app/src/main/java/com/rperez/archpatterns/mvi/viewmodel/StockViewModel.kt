package com.rperez.archpatterns.mvi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.rperez.archpatterns.BuildConfig
import com.rperez.archpatterns.mvi.data.FinnhubWebSocketClient
import com.rperez.archpatterns.mvi.model.StockIntent
import com.rperez.archpatterns.mvi.model.StockState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

class StockViewModel : ViewModel() {

    private val apiKey = BuildConfig.FINNHUB_API_KEY

    private val _state = MutableStateFlow(StockState())
    val state: StateFlow<StockState> = _state

    private val socketClient = FinnhubWebSocketClient(
        onMessage = { handleMessage(it) },
        onError = { _state.update { state -> state.copy(error = it.message) } },
        onConnected = { _state.update { state -> state.copy(isConnected = true) } },
    )

    fun process(intent: StockIntent) {
        when (intent) {
            is StockIntent.Connect -> socketClient.connect(apiKey)
            is StockIntent.Disconnect -> socketClient.disconnect()
            is StockIntent.SubscribeToSymbol -> socketClient.send("""{"type":"subscribe","symbol":"${intent.symbol}"}""")
        }
    }

    private fun handleMessage(message: String) {
        Log.d("StockViewModel", "Received message: $message")

        val json = JSONObject(message)
        val data = json.optJSONArray("data") ?: return

        val newPrices = mutableListOf<Float>()

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val symbol = obj.getString("s")
            if (symbol == "TSM") {
                val price = obj.getDouble("p").toFloat()
                newPrices += price
            }
        }

        if (newPrices.isNotEmpty()) {
            _state.update {
                val updatedPrices = (it.prices + newPrices).takeLast(50)
                it.copy(prices = it.prices + updatedPrices)
            }
        }
    }
}