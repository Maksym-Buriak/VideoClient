package com.maks_buriak.videoclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks_buriak.videoclient.domain.usecase.IsNickNameTakenUseCase
import com.maks_buriak.videoclient.domain.usecase.UpdateUserNickUseCase
import com.maks_buriak.videoclient.presentation.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NickNameViewModel(
    private val updateUserNickUseCase: UpdateUserNickUseCase,
    private val isNickNameTakenUseCase: IsNickNameTakenUseCase,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<NickNameUiState>(NickNameUiState.Idle)
    val uiState: StateFlow<NickNameUiState> = _uiState

    fun saveNickName(uid: String, nickName: String) {
        viewModelScope.launch {
            _uiState.value = NickNameUiState.Loading

            val trimmedNick = nickName.trim()

            if (trimmedNick.isBlank()) {
                _uiState.value = NickNameUiState.Error("Нік не може бути порожнім!")
                return@launch
            }

            if (trimmedNick.contains(" ")) {
                _uiState.value = NickNameUiState.Error("Нік не може містити пробіли")
                return@launch
            }

            val regex = Regex("^[A-Za-z0-9_]+$")
            if (!regex.matches(trimmedNick)) {
                _uiState.value = NickNameUiState.Error("Нік може містити тільки латинські літери, цифри та символ '_'")
                return@launch
            }

            if (trimmedNick.length < 3) {
                _uiState.value = NickNameUiState.Error("Нік повинен містити щонайменше 3 символи")
                return@launch
            }

            if (trimmedNick.length > 20) {
                _uiState.value = NickNameUiState.Error("Нік не може перевищувати 20 символів")
                return@launch
            }

            val taken = isNickNameTakenUseCase(trimmedNick)
            if (taken) {
                _uiState.value = NickNameUiState.Error("Цей нік вже зайнятий")
            } else {
                updateUserNickUseCase(uid, trimmedNick)
                userManager.updateCachedNickName(trimmedNick)
                _uiState.value = NickNameUiState.Success
            }
        }
    }
}

sealed class NickNameUiState {
    object Idle : NickNameUiState()
    object Loading : NickNameUiState()
    object Success : NickNameUiState()
    data class Error(val message: String) : NickNameUiState()
}