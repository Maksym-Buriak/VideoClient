package com.maks_buriak.videoclient.domain.repository

interface PhoneAuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, activityProvider: () -> Any): Result<String> // return verificationId
    suspend fun verifyCode(verificationId: String, code: String): Result<Unit>
}