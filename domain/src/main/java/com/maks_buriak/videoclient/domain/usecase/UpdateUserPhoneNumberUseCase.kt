package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.UserRepository

class UpdateUserPhoneNumberUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(uid: String, phoneNumber: String) = repository.updateUserPhoneNumber(uid, phoneNumber)
}