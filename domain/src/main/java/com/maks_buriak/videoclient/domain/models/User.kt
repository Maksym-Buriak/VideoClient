package com.maks_buriak.videoclient.domain.models

data class User (
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val phoneNumber: String?,
    val nickName: String?
)