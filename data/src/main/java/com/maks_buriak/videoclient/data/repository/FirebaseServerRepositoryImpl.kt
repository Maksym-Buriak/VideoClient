package com.maks_buriak.videoclient.data.repository

import androidx.core.graphics.values
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.maks_buriak.videoclient.data.storage.models.FirebaseServerDto
import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.domain.repository.ServerRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseServerRepositoryImpl : ServerRepository {
    private val database = FirebaseDatabase.getInstance()

    override fun getServerFlow(): Flow<Result<List<VideoServer>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dto = snapshot.getValue(FirebaseServerDto::class.java)

                    if (dto != null) {
                        // Оскільки це один об'єкт, повертаємо список з одним елементом
                        trySend(Result.success(listOf(dto.toDomain())))
                    } else {
                        // Якщо даних немає, порожній список
                        trySend(Result.success(emptyList()))
                    }
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        val ref = database.getReference("server")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }
}