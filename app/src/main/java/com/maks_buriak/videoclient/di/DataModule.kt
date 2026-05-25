package com.maks_buriak.videoclient.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.maks_buriak.videoclient.data.repository.FirebaseAuthRepositoryImpl
import com.maks_buriak.videoclient.data.repository.FirebaseServerRepositoryImpl
import com.maks_buriak.videoclient.data.repository.MessageRepositoryImpl
import com.maks_buriak.videoclient.data.repository.PhoneAuthRepositoryImpl
import com.maks_buriak.videoclient.data.repository.UserRepositoryImpl
import com.maks_buriak.videoclient.data.repository.VideoStreamRepositoryImpl
import com.maks_buriak.videoclient.data.storage.firebase.FirebaseMessageStorage
import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository
import com.maks_buriak.videoclient.domain.repository.MessageRepository
import com.maks_buriak.videoclient.domain.repository.PhoneAuthRepository
import com.maks_buriak.videoclient.domain.repository.ServerRepository
import com.maks_buriak.videoclient.domain.repository.UserRepository
import com.maks_buriak.videoclient.domain.repository.VideoStreamRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val dataModule = module {

    // Realtime Database
    single { Firebase.database }

    single { FirebaseMessageStorage(get()) }

    single<MessageRepository> {
        MessageRepositoryImpl(storage = get()) //імплементація інтерфейсу
    }

    // FirebaseAuth
    single { FirebaseAuth.getInstance() } //реєструється екземпляр FirebaseAuth
    single<FirebaseAuthRepository> { FirebaseAuthRepositoryImpl(get()) }

    // Firestore
    single { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get()) }

    // PhoneAuthRepository
    single<PhoneAuthRepository> {
        PhoneAuthRepositoryImpl(
            firebaseAuth = get()
        )
    }

    // VideoStreamRepository
    single { OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build() }
    single<VideoStreamRepository> { VideoStreamRepositoryImpl() }

    single<ServerRepository> { FirebaseServerRepositoryImpl() }
}