package com.maks_buriak.videoclient.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maks_buriak.videoclient.presentation.viewmodel.NickNameUiState
import com.maks_buriak.videoclient.presentation.viewmodel.NickNameViewModel

@Composable
fun NickNameScreen(
    uid: String,
    viewModel: NickNameViewModel,
    onNickSaved: () -> Unit
) {
    var nickName by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Введіть ваш NickName")
        Spacer(Modifier.height(8.dp))

        TextField(
            value = nickName,
            onValueChange = { nickName = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveNickName(uid, nickName) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зберегти")
        }

        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is NickNameUiState.Loading -> CircularProgressIndicator()
            is NickNameUiState.Success -> {
                Text("Нік збережено ✅")
                onNickSaved()
            }
            is NickNameUiState.Error -> Text(
                (uiState as NickNameUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            else -> {}
        }
    }
}