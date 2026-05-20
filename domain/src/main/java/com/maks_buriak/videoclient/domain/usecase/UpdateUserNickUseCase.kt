package com.maks_buriak.videoclient.domain.usecase

import com.maks_buriak.videoclient.domain.repository.UserRepository

class UpdateUserNickUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(uid: String, nickName: String) =
        repository.updateUserNick(uid, nickName)
}