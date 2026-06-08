package com.maks_buriak.videoclient.presentation.viewmodel

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.domain.usecase.GetAvailableServersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServerSelectionViewModel(
    private val getAvailableServersUseCase: GetAvailableServersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServerUiState>(ServerUiState.Loading)
    val uiState: StateFlow<ServerUiState> = _uiState

    fun loadServers() {
        viewModelScope.launch {
            _uiState.value = ServerUiState.Loading
            getAvailableServersUseCase().collect { result ->
                result.onSuccess { servers ->
                    _uiState.value = ServerUiState.Success(servers)
                }.onFailure { error ->
                    _uiState.value = ServerUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }

    fun selectServer(server: VideoServer, onNavigate: (VideoServer) -> Unit) {
        if (server.status == "online") {
            onNavigate(server)
        } else {
            viewModelScope.launch {
                val previousState = _uiState.value
                _uiState.value = ServerUiState.Error("Сервер офлайн")
                kotlinx.coroutines.delay(2000)
                _uiState.value = previousState
            }
        }
    }
}

sealed class ServerUiState {
    object Loading : ServerUiState()
    data class Success(val servers: List<VideoServer>) : ServerUiState()
    data class Error(val message: String) : ServerUiState()
}