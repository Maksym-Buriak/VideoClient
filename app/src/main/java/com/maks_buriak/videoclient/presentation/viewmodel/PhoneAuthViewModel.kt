package com.maks_buriak.videoclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks_buriak.videoclient.domain.usecase.GetCurrentUserUseCase
import com.maks_buriak.videoclient.domain.usecase.IsPhoneNumberTakenUseCase
import com.maks_buriak.videoclient.domain.usecase.SendVerificationCodeUseCase
import com.maks_buriak.videoclient.domain.usecase.UpdateUserPhoneNumberUseCase
import com.maks_buriak.videoclient.domain.usecase.VerifyCodeUseCase
import com.maks_buriak.videoclient.presentation.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhoneAuthViewModel(
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserPhoneNumberUseCase: UpdateUserPhoneNumberUseCase,
    private val isPhoneNumberTakenUseCase: IsPhoneNumberTakenUseCase,
    private val userManager: UserManager
) : ViewModel() {

    private var verificationId: String? = null

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    private val _codeSent = MutableStateFlow(false)
    val codeSent: StateFlow<Boolean> = _codeSent

    private val _isVerified = MutableStateFlow(false) //контролює перехід після успішної верифікації
    val isVerified: StateFlow<Boolean> = _isVerified

    private val _isSending = MutableStateFlow(false) //керує станом кнопок
    val isSending: StateFlow<Boolean> = _isSending

    fun sendCode(phoneNumber: String, activityProvider: () -> Any) = viewModelScope.launch {
        _isSending.value = true
        val taken = isPhoneNumberTakenUseCase(phoneNumber)
        if (taken) {
            _status.value = "Цей номер телефону вже використовується"
            _codeSent.value = false
            _isSending.value = false
            return@launch
        }

        val result = sendVerificationCodeUseCase(phoneNumber, activityProvider)
        result.onSuccess { id ->
            verificationId = id
            _status.value = "Код відправлено на номер $phoneNumber"
            _codeSent.value = true
        }.onFailure {
            _status.value = "Помилка відправки коду: ${it.message}"
            _codeSent.value = false
        }
        _isSending.value = false
    }

    fun verifyCode(code: String, phoneNumber: String) = viewModelScope.launch {
        _isSending.value = true
        verificationId?.let { id ->
            val result = verifyCodeUseCase(id, code)
            result.onSuccess {
                // Updating the user phone number
                val currentUser = getCurrentUserUseCase()
                currentUser?.let { user ->

                    updateUserPhoneNumberUseCase(user.uid, phoneNumber)
                    userManager.refreshUser()
                    _isVerified.value = true
                }
            }.onFailure {
                _status.value = "Неправильний код: ${it.message}"
            }
        } ?: run {
            _status.value = "Спочатку надішліть код"
        }
        _isSending.value = false
    }
}