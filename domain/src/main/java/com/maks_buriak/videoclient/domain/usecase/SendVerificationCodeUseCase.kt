package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.PhoneAuthRepository

class SendVerificationCodeUseCase(private val repository: PhoneAuthRepository) {
    suspend operator fun invoke(phoneNumber: String, activityProvider: () -> Any): Result<String> = repository.sendVerificationCode(phoneNumber, activityProvider)
}