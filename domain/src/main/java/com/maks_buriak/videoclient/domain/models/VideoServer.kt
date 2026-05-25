package com.maks_buriak.videoclient.domain.models

data class VideoServer(
    val address: String,
    val status: String,
    val lastSeen: Long
)