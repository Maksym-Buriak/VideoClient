package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow

class GetAvailableServersUseCase(private val repository: ServerRepository) {
    operator fun invoke(): Flow<Result<List<VideoServer>>> {
        return repository.getServerFlow()
    }
}