package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.PhoneAuthRepository

class VerifyCodeUseCase(private val repository: PhoneAuthRepository) {
    suspend operator fun invoke(verificationId: String, code: String): Result<Unit> =
        repository.verifyCode(verificationId, code)
}