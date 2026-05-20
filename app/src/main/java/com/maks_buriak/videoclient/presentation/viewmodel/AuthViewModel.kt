package com.maks_buriak.videoclient.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.maks_buriak.videoclient.data.authentication.google.GoogleSignInHelper
import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.usecase.GetUserByUidUseCase
import com.maks_buriak.videoclient.domain.usecase.SaveUserToFirestoreUseCase
import com.maks_buriak.videoclient.domain.usecase.SignInWithGoogleUseCase
import com.maks_buriak.videoclient.presentation.UserManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val googleSignInHelper: GoogleSignInHelper,
    private val saveUserToFirestoreUseCase: SaveUserToFirestoreUseCase,
    private val userManager: UserManager,
    private val getUserByUidUseCase: GetUserByUidUseCase
) : ViewModel() {

    val userState: StateFlow<User?> = userManager.currentUser

    fun startGoogleSignIn(): Intent {
        return googleSignInHelper.getSignInIntent()
    }

    fun handleGoogleSignInResult(data: Intent?) {
        val task = googleSignInHelper.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                onGoogleSignIn(idToken)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }


    private fun onGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(idToken)
            result.onSuccess { authUserResult ->

                val user = authUserResult.user

                if (authUserResult.isNewUser) {
                    saveUserToFirestoreUseCase(user)
                }

                userManager.refreshUser()

            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}