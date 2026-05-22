package com.maks_buriak.videoclient.data.repository

import android.util.Log
import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.connection.Exchange
import okio.ByteString.Companion.toByteString
import kotlin.coroutines.resume

class VideoStreamRepositoryImpl(private val okHttpClient: OkHttpClient) : VideoStreamRepository {

    private var webSocket: WebSocket? = null
    private var isConnected = false

    override suspend fun startStream(serverUrl: String): Result<Unit> = suspendCancellableCoroutine { continuation ->
        val request = Request.Builder().url(serverUrl).build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                Log.d("VideoStreamRepo", "WebSocket Connected to $serverUrl")
                if (continuation.isActive) continuation.resume(Result.success(Unit))
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.e("VideoStreamRepo", "WebSocket Connection Failed", t)
                if (continuation.isActive) continuation.resume(Result.failure(t))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                isConnected = false
                Log.d("VideoStreamRepo", "WebSocket Closing: $reason")
            }
        })
    }

    override suspend fun sendFrame(frameBytes: ByteArray): Result<Unit> {
        val socket = webSocket
        return if (socket != null && isConnected) {
            val sent = socket.send(frameBytes.toByteString())
            if (sent) Result.success(Unit) else Result.failure(Exception("Failed to send frame bytes"))
        } else {
            Result.failure(Exception("WebSocket is not connected"))
        }
    }

    override suspend fun stopStream() {
        webSocket?.close(1000, "Stream stopped by user")
        webSocket = null
        isConnected = false
    }
}