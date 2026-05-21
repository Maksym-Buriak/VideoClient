package com.maks_buriak.videoclient.di

import com.maks_buriak.videoclient.domain.usecase.GetCurrentUserUseCase
import com.maks_buriak.videoclient.domain.usecase.GetUserByUidUseCase
import com.maks_buriak.videoclient.domain.usecase.IsNickNameTakenUseCase
import com.maks_buriak.videoclient.domain.usecase.IsPhoneNumberTakenUseCase
import com.maks_buriak.videoclient.domain.usecase.SaveUserToFirestoreUseCase
import com.maks_buriak.videoclient.domain.usecase.SendMessageUseCase
import com.maks_buriak.videoclient.domain.usecase.SendVerificationCodeUseCase
import com.maks_buriak.videoclient.domain.usecase.SendVideoFrameUseCase
import com.maks_buriak.videoclient.domain.usecase.SignInWithGoogleUseCase
import com.maks_buriak.videoclient.domain.usecase.SignOutUseCase
import com.maks_buriak.videoclient.domain.usecase.StartVideoStreamUseCase
import com.maks_buriak.videoclient.domain.usecase.StopVideoStreamUseCase
import com.maks_buriak.videoclient.domain.usecase.UpdateUserNickUseCase
import com.maks_buriak.videoclient.domain.usecase.UpdateUserPhoneNumberUseCase
import com.maks_buriak.videoclient.domain.usecase.VerifyCodeUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<SendMessageUseCase> {
        SendMessageUseCase(repository = get())
    }

    factory<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(repository = get())
    }

    factory<SignOutUseCase> {
        SignOutUseCase(repository = get())
    }

    factory<GetCurrentUserUseCase> {
        GetCurrentUserUseCase(repository = get())
    }

    factory<SaveUserToFirestoreUseCase> {
        SaveUserToFirestoreUseCase(repository = get())
    }

    factory<SendVerificationCodeUseCase> {
        SendVerificationCodeUseCase(repository = get())
    }

    factory<VerifyCodeUseCase> {
        VerifyCodeUseCase(repository = get())
    }

    factory<UpdateUserPhoneNumberUseCase> {
        UpdateUserPhoneNumberUseCase(repository = get())
    }

    factory<IsPhoneNumberTakenUseCase> {
        IsPhoneNumberTakenUseCase(repository = get())
    }

    factory<UpdateUserNickUseCase> {
        UpdateUserNickUseCase(repository = get())
    }

    factory<GetUserByUidUseCase> {
        GetUserByUidUseCase(repository = get())
    }

    factory<IsNickNameTakenUseCase> {
        IsNickNameTakenUseCase(repository = get())
    }

    factory<StartVideoStreamUseCase> {
        StartVideoStreamUseCase(repository = get())
    }

    factory<SendVideoFrameUseCase> {
        SendVideoFrameUseCase(repository = get())
    }

    factory<StopVideoStreamUseCase> {
        StopVideoStreamUseCase(repository = get())
    }
}