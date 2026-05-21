package com.maks_buriak.videoclient.domain.repository

interface VideoStreamRepository {
    suspend fun startStream(serverUrl: String): Result<Unit>
    suspend fun sendFrame(frameBytes: ByteArray): Result<Unit>
    suspend fun stopStream()
}