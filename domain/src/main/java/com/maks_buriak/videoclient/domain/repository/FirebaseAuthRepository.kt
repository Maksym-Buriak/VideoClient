package com.maks_buriak.videoclient.domain.repository

import com.maks_buriak.videoclient.domain.models.AuthUserResult
import com.maks_buriak.videoclient.domain.models.User

interface FirebaseAuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<AuthUserResult>

    fun getCurrentUser(): User?
    fun signOut()
    fun addAuthStateListener(listener: () -> Unit)
}