package com.maks_buriak.videoclient.domain.repository

import com.maks_buriak.videoclient.domain.models.VideoServer
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getServerFlow(): Flow<Result<List<VideoServer>>>
}