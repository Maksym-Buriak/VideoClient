package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.domain.repository.ServerRepository

class GetAvailableServersUseCase(private val repository: ServerRepository) {
    suspend operator fun invoke(): Result<List<VideoServer>> {
        return repository.getAvailableServers()
    }
}