package com.maks_buriak.videoclient.di

import com.maks_buriak.videoclient.R
import com.maks_buriak.videoclient.data.authentication.google.GoogleSignInHelper
import com.maks_buriak.videoclient.presentation.UserManager
import com.maks_buriak.videoclient.presentation.viewmodel.AuthViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.MainViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.NickNameViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.PhoneAuthViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.ServerSelectionViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.StreamVideoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        GoogleSignInHelper(
            context = androidContext(),
            webClientId = androidContext().getString(R.string.default_web_client_id)
        )
    }

    // UserManager singleton for global user state
    single { UserManager(repository = get(), getUserByUidUseCase = get()) }

    viewModel<MainViewModel> {
        MainViewModel(
            userManager = get()
        )
    }

    viewModel<AuthViewModel> {
        AuthViewModel(
            signInWithGoogleUseCase = get(),
            googleSignInHelper = get(),
            saveUserToFirestoreUseCase = get(),
            userManager = get(),
            getUserByUidUseCase = get()
        )
    }

    viewModel<PhoneAuthViewModel> {
        PhoneAuthViewModel(
            sendVerificationCodeUseCase = get(),
            verifyCodeUseCase = get(),
            getCurrentUserUseCase = get(),
            updateUserPhoneNumberUseCase = get(),
            isPhoneNumberTakenUseCase = get(),
            userManager = get()
        )
    }

    viewModel<NickNameViewModel> {
        NickNameViewModel(
            updateUserNickUseCase = get(),
            isNickNameTakenUseCase = get(),
            userManager = get()
        )
    }

    viewModel<StreamVideoViewModel> {
        StreamVideoViewModel(
            startVideoStreamUseCase = get(),
            sendVideoFrameUseCase = get(),
            stopVideoStreamUseCase = get()
        )
    }

    viewModel<ServerSelectionViewModel> {
        ServerSelectionViewModel(
            getAvailableServersUseCase = get()
        )
    }
}