package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository

class SignOutUseCase(private val repository: FirebaseAuthRepository) {
    operator fun invoke() {
        repository.signOut()
    }
}