package com.maks_buriak.videoclient.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.maks_buriak.videoclient.domain.repository.PhoneAuthRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class PhoneAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : PhoneAuthRepository {

    private var lastVerificationId: String? = null

    override suspend fun sendVerificationCode(phoneNumber: String, activityProvider: () -> Any): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            val activity = activityProvider() as Activity
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity) // Activity is passed here via lambda
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        continuation.resume(Result.success(credential.smsCode ?: ""))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        continuation.resume(Result.failure(e))
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        lastVerificationId = verificationId
                        continuation.resume(Result.success(verificationId))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun verifyCode(verificationId: String, code: String): Result<Unit> {
        val id = verificationId.takeIf { it.isNotEmpty() } ?: lastVerificationId
        if (id == null) return Result.failure(Exception("Спочатку надішліть код"))
        return try {
            val credential = PhoneAuthProvider.getCredential(id!!, code)

            val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception("User is not signed in"))

            if (currentUser.phoneNumber.isNullOrEmpty()) {
                // Якщо номеру ще немає -> додаємо
                currentUser.linkWithCredential(credential).await()
            } else {
                // Якщо номер уже є -> оновлюємо
                currentUser.updatePhoneNumber(credential).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}