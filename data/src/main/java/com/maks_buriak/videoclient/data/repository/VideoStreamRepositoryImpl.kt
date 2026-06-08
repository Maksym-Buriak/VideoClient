package com.maks_buriak.videoclient.data.repository

import android.util.Log
import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.URISyntaxException
import kotlin.coroutines.resume

class VideoStreamRepositoryImpl : VideoStreamRepository {

    private var socket: Socket? = null

    override suspend fun startStream(serverUrl: String): Result<Unit> = suspendCancellableCoroutine { continuation ->
        try {
            // Створюємо сокет з правильними налаштуваннями
            val options = IO.Options.builder()
                .setTransports(arrayOf("websocket")) // Використовуємо тільки WebSocket
                .build()

            socket = IO.socket(serverUrl, options)

            // Налаштовуємо обробники подій
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("VideoStreamRepo", "Socket.IO Connected to $serverUrl")
                if (continuation.isActive) {
                    continuation.resume(Result.success(Unit))
                }
            }

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                val error = args.getOrNull(0) as? Exception
                Log.e("VideoStreamRepo", "Socket.IO Connection Error", error)
                
                val message = if (error is io.socket.engineio.client.EngineIOException) {
                    "Час очікування вичерпано. Перевірте підключення до Tailscale VPN"
                } else {
                    "Помилка з'єднання з сервером."
                }
                
                if (continuation.isActive) {
                    continuation.resume(Result.failure(Exception(message)))
                }
                socket?.disconnect()
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("VideoStreamRepo", "Socket.IO Disconnected")
            }

            // Запускаємо підключення
            socket?.connect()

        } catch (e: URISyntaxException) {
            Log.e("VideoStreamRepo", "Invalid Server URL", e)
            if (continuation.isActive) {
                continuation.resume(Result.failure(e))
            }
        }
    }

    override suspend fun sendFrame(frameBytes: ByteArray): Result<Unit> {
        val currentSocket = socket
        return if (currentSocket != null && currentSocket.connected()) {
            // Надсилаємо дані на подію 'video_frame', як очікує сервер
            currentSocket.emit("video_frame", frameBytes)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Socket.IO is not connected"))
        }
    }

    override suspend fun stopStream() {
        socket?.disconnect()
        socket = null
    }
}