package com.maks_buriak.videoclient.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maks_buriak.videoclient.domain.models.VideoServer
import com.maks_buriak.videoclient.presentation.viewmodel.ServerSelectionViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.ServerUiState

@Composable
fun ServerSelectionContent(
    viewModel: ServerSelectionViewModel,
    onServerSelected: (VideoServer) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(max = 400.dp)
    ) {
        Text(
            text = "Виберіть сервер для трансляції",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is ServerUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ServerUiState.Error -> {
                Text("Помилка: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
            is ServerUiState.Success -> {
                LazyColumn {
                    items(state.servers) { server ->
                        ServerItem(server, onClick = { viewModel.selectServer(server, onServerSelected) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ServerItem(server: VideoServer, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(server.address, fontWeight = FontWeight.Bold) },
        supportingContent = {
            Text(
                text = "Статус: ${server.status}",
                color = if (server.status == "online") Color(0xFF4CAF50) else Color.Red
            )
        }
    )
}
