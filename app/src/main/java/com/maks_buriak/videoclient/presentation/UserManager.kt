package com.maks_buriak.videoclient.presentation

import com.google.firebase.auth.FirebaseAuth
import com.maks_buriak.videoclient.domain.models.User
import com.maks_buriak.videoclient.domain.repository.FirebaseAuthRepository
import com.maks_buriak.videoclient.domain.usecase.GetUserByUidUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserManager(
    private val repository: FirebaseAuthRepository,
    private val getUserByUidUseCase: GetUserByUidUseCase
) {

    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        scope.launch {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                _currentUser.value = null
            } else {
                try {
                    firebaseUser.reload().await()
                    _currentUser.value = repository.getCurrentUser()
                } catch (e: Exception) {
                    _currentUser.value = null
                }
            }
        }
    }

    init {
        auth.addAuthStateListener(listener)
        scope.launch { refreshUser() } // ініціалізація стану при запуску
    }

    // Оновлюємо стан користувача з репозиторію
    suspend fun refreshUser() {
        try {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                _currentUser.value = null
            } else {
                firebaseUser.reload().await()  // Перевірка стану в Firebase
                val user = repository.getCurrentUser()
                val updatedUser = user?.uid?.let { getUserByUidUseCase(it) }
                _currentUser.value = updatedUser ?: user
            }
        } catch (e: Exception) {
            _currentUser.value = null
        }
    }

    fun updateCachedNickName(newNick: String) {
        val current = _currentUser.value
        if (current != null) {
            _currentUser.value = current.copy(nickName = newNick)
        }
    }

    fun logout() {
        repository.signOut()
        _currentUser.value = null
    }
}