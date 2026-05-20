package com.maks_buriak.videoclient.data.storage.models

import com.google.firebase.auth.FirebaseUser
import com.maks_buriak.videoclient.domain.models.User

data class FirebaseUserDto(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val nickName: String? = null
) {
    companion object {
        fun fromFirebaseUser(user: FirebaseUser) = FirebaseUserDto(
            uid = user.uid,
            displayName = user.displayName,
            email = user.email,
            photoUrl = user.photoUrl?.toString(),
            phoneNumber = user.phoneNumber,
            nickName = null
        )
    }

    fun toDomain(): User {
        return User(
            uid = uid,
            displayName = displayName,
            email = email,
            photoUrl = photoUrl,
            phoneNumber = phoneNumber,
            nickName = nickName
        )
    }
}