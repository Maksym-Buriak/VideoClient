package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository

class StopVideoStreamUseCase(private val repository: VideoStreamRepository) {
    suspend operator fun invoke() = repository.stopStream()
}