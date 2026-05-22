package com.maks_buriak.videoclient.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks_buriak.videoclient.domain.usecase.SendVideoFrameUseCase
import com.maks_buriak.videoclient.domain.usecase.StartVideoStreamUseCase
import com.maks_buriak.videoclient.domain.usecase.StopVideoStreamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StreamVideoViewModel(
    private val startVideoStreamUseCase: StartVideoStreamUseCase,
    private val sendVideoFrameUseCase: SendVideoFrameUseCase,
    private val stopVideoStreamUseCase: StopVideoStreamUseCase
) : ViewModel() {

    private val DEFAULT_SERVER_URL = "ws://192.168.0.103:5000"

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> get() = _isStreaming

    fun toggleStream() {
        viewModelScope.launch {
            if (_isStreaming.value) {
                stopVideoStreamUseCase()
                _isStreaming.value = false
            } else {
                val result = startVideoStreamUseCase(DEFAULT_SERVER_URL)
                if (result.isSuccess) {
                    _isStreaming.value = true
                }
            }
        }
    }

    fun sendFrame(bytes: ByteArray) {
        if (!_isStreaming.value) return
        viewModelScope.launch {
            sendVideoFrameUseCase(bytes)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { stopVideoStreamUseCase }
    }
}