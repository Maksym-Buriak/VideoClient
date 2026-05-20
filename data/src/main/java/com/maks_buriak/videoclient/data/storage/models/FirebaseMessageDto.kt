package com.maks_buriak.videoclient.data.storage.models

//спеціальна модель для роботи з Firebase, не впливає на domain модель.
data class FirebaseMessageDto (
    val id: String = "",
    val text: String = ""
)