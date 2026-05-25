package com.maks_buriak.videoclient.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.maks_buriak.videoclient.data.storage.models.FirebaseServerDto
import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.domain.repository.ServerRepository
import kotlinx.coroutines.tasks.await

class FirebaseServerRepositoryImpl : ServerRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun getAvailableServers(): Result<List<VideoServer>> {
        return try {
            val snapshot = database.getReference("server").get().await()

            val dto = snapshot.getValue(FirebaseServerDto::class.java)

            val servers = if (dto != null) {
                listOf(dto.toDomain())
            } else {
                emptyList()
            }
            
            Result.success(servers)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}