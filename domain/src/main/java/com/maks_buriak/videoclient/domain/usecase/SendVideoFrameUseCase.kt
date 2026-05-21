package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository

class SendVideoFrameUseCase(private val repository: VideoStreamRepository) {
    suspend operator fun invoke(frameBytes: ByteArray) = repository.sendFrame(frameBytes)
}