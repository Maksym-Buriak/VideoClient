package com.maks_buriak.videoclient.domain.repository

import com.maks_buriak.videoclient.domain.models.VideoServer

interface ServerRepository {
    suspend fun getAvailableServers(): Result<List<VideoServer>>
}