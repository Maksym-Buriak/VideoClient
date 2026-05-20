package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.Message
import com.maks_buriak.videoclient.domain.repository.MessageRepository

class SendMessageUseCase(private val repository: MessageRepository) {

    suspend operator fun invoke(message: Message) {
        repository.sendMessage(message)
    }
}