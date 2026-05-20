package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.UserRepository

class SaveUserToFirestoreUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}