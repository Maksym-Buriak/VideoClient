package com.maks_buriak.videoclient.domain.repository

import com.maks_buriak.videoclient.domain.models.User

//interface for Firebase Firestore DB
interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUserByUid(uid: String): User?
    suspend fun updateUserPhoneNumber(uid: String, phoneNumber: String)
    suspend fun isPhoneNumberTaken(phoneNumber: String): Boolean

    suspend fun updateUserNick(uid: String, nickName: String)
    suspend fun isNickNameTaken(nickName: String): Boolean
}