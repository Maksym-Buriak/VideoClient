package com.maks_buriak.videoclient.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.maks_buriak.videoclient.data.storage.models.FirebaseUserDto
import com.maks_buriak.videoclient.domain.models.AuthUserResult
import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthRepository {
    override suspend fun signInWithGoogle(idToken: String): Result<AuthUserResult> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("User is null"))

            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            val user = FirebaseUserDto.fromFirebaseUser(firebaseUser).toDomain()
            Result.success(AuthUserResult(user = user, isNewUser = isNewUser))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val user = firebaseAuth.currentUser ?: return null
        return FirebaseUserDto.fromFirebaseUser(user).toDomain()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun addAuthStateListener(listener: () -> Unit) {
        firebaseAuth.addAuthStateListener { listener() }
    }

    fun FirebaseUserDto.toDomain(): User {
        return User(
            uid = uid,
            displayName = displayName,
            email = email,
            photoUrl = photoUrl,
            phoneNumber = phoneNumber,
            nickName = nickName
        )
    }
}