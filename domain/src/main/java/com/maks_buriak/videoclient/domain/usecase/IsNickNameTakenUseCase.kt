package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.UserRepository

class IsNickNameTakenUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(nickName: String): Boolean =
        repository.isNickNameTaken(nickName)
}