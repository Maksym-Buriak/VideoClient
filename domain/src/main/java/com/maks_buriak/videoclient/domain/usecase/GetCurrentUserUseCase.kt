package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository

class GetCurrentUserUseCase(private val repository: FirebaseAuthRepository) {
    operator fun invoke(): User? {
        return repository.getCurrentUser()
    }
}