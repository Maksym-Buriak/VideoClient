package com.maks_buriak.videoclient.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.maks_buriak.videoclient.data.storage.models.FirebaseUserDto
import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val firestore: FirebaseFirestore) : UserRepository {

    override suspend fun saveUser(user: User) {
        val userMap = hashMapOf(
            "uid" to user.uid,
            "displayName" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl,
            "phoneNumber" to user.phoneNumber,
            "nickName" to user.nickName,
            "createdAt" to FieldValue.serverTimestamp() // серверний час
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userMap, SetOptions.merge())
            .await()
    }

    override suspend fun getUserByUid(uid: String): User? {
        val snapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()
        val dto = snapshot.toObject(FirebaseUserDto::class.java)
        return dto?.toDomain()
    }

    override suspend fun updateUserPhoneNumber(uid: String, phoneNumber: String) {
        firestore.collection("users")
            .document(uid)
            .update("phoneNumber", phoneNumber)
            .await()
    }

    override suspend fun isPhoneNumberTaken(phoneNumber: String): Boolean {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .await()
        return !querySnapshot.isEmpty
    }

    override suspend fun updateUserNick(uid: String, nickName: String) {
        firestore.collection("users")
            .document(uid)
            .update("nickName", nickName)
            .await()
    }

    override suspend fun isNickNameTaken(nickName: String): Boolean {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("nickName", nickName)
            .get()
            .await()
        return !querySnapshot.isEmpty
    }
}