package com.maks_buriak.videoclient.data.repository

import com.maks_buriak.videoclient.data.storage.firebase.FirebaseMessageStorage
import com.maks_buriak.videoclient.data.storage.models.FirebaseMessageDto
import com.maks_buriak.videoclient.domain.models.Message
import com.maks_buriak.videoclient.domain.repository.MessageRepository

class MessageRepositoryImpl(private val storage: FirebaseMessageStorage) : MessageRepository {

    override suspend fun sendMessage(message: Message) {
        val dto = message.toFirebaseDto()
        storage.writeMessage(dto)
    }

    fun FirebaseMessageDto.toDomain(): Message {
        return Message(id = id, text = text)
    }

    fun Message.toFirebaseDto(): FirebaseMessageDto {
        return FirebaseMessageDto(id = id, text = text)
    }
}