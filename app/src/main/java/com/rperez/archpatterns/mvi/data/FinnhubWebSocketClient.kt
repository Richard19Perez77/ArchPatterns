package com.rperez.archpatterns.mvi.data

import com.rperez.archpatterns.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class FinnhubWebSocketClient(
    private val onMessage: (String) -> Unit,
    private val onError: (Throwable) -> Unit,
    private val onConnected: () -> Unit,
) {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connect(apiKey: String) {
        val request = okhttp3.Request.Builder()
            .url("wss://ws.finnhub.io?token=$apiKey")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) = onConnected()
            override fun onMessage(webSocket: WebSocket, text: String) = onMessage(text)
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) =
                onError(t)
        })
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }
}