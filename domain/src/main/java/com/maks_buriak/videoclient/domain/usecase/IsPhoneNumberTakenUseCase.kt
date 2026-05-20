package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.UserRepository

class IsPhoneNumberTakenUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(phoneNumber: String): Boolean {
        return repository.isPhoneNumberTaken(phoneNumber)
    }
}