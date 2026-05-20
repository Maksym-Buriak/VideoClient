package com.maks_buriak.videoclient.domain.repository

import com.maks_buriak.videoclient.domain.models.Message

interface MessageRepository {
    suspend fun sendMessage (message: Message)
}