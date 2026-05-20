package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.UserRepository

class GetUserByUidUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(uid: String): User? {
        return repository.getUserByUid(uid)
    }
}