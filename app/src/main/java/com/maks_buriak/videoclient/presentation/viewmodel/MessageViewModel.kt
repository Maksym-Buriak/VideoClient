package com.maks_buriak.videoclient.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maks_buriak.videoclient.domain.models.Message
import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.usecase.SendMessageUseCase
import com.maks_buriak.videoclient.presentation.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MessageViewModel(
    private val userManager: UserManager
) : ViewModel() {

    val currentUser: StateFlow<User?> = userManager.currentUser

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> get() = _uiMessage


    fun signOut() {
        userManager.logout()
    }

    fun checkPhoneVerification(onNeedVerification: () -> Unit) {
        
        val action = getPhoneAction()

        when (action) {
            PhoneAction.ADD -> {
                onNeedVerification()
                _uiMessage.value = "Додайте свій номер телефону"
            }
            PhoneAction.CHANGE -> {
                onNeedVerification()
                _uiMessage.value = "Для зміни номера телефону введіть його у відповідне поле та підтвердіть"
            }
        }
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    enum class PhoneAction {
        ADD, CHANGE
    }

    fun getPhoneAction(): PhoneAction {
        return if (currentUser.value?.phoneNumber.isNullOrEmpty()) PhoneAction.ADD
        else PhoneAction.CHANGE
    }


    enum class NickAction {
        ADD, CHANGE
    }

    fun getNickAction(): NickAction {
        return if (currentUser.value?.nickName.isNullOrEmpty()) NickAction.ADD
        else NickAction.CHANGE
    }

    fun checkNickChange(onNeedNick: () -> Unit) {
        val action = getNickAction()

        when (action) {
            NickAction.ADD -> {
                onNeedNick()
                _uiMessage.value = "Додайте свій нік"
            }
            NickAction.CHANGE -> {
                onNeedNick()
                _uiMessage.value = "Для зміни ніку введіть його у відповідне поле та підтвердіть"
            }
        }
    }
}