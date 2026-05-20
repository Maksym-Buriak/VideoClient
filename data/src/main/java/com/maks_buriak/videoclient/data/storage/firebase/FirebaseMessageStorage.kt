package com.maks_buriak.videoclient.data.storage.firebase

import com.google.firebase.database.FirebaseDatabase
import com.maks_buriak.videoclient.data.storage.models.FirebaseMessageDto
import kotlinx.coroutines.tasks.await

class FirebaseMessageStorage(private val database: FirebaseDatabase) {

    suspend fun writeMessage(dto: FirebaseMessageDto) {
        //підключення до бази даних
        val reference = database.getReference("messages").child(dto.id)
        reference.setValue(dto).await()
    }
}