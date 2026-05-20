package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository

class SignInWithGoogleUseCase(private val repository: FirebaseAuthRepository) {
    suspend operator fun invoke(idToken: String) = repository.signInWithGoogle(idToken)
}