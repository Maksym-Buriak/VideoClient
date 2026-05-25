package com.maks_buriak.videoclient.data.storage.models

import com.maks_buriak.videoclient.domain.models.VideoServer

class FirebaseServerDto(
    val address: String? = null,
    val status: String? = null,
    val last_seen: Long? = null
) {
    fun toDomain() = VideoServer(
        address = address ?: "",
        status = status ?: "offline",
        lastSeen = last_seen ?: 0L
    )
}