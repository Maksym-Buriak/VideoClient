package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository

class StartVideoStreamUseCase(private val repository: VideoStreamRepository) {
    suspend operator fun invoke(serverUrl: String) = repository.startStream(serverUrl)
}