package com.maks_buriak.videoclient.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.maks_buriak.videoclient.R
import com.maks_buriak.videoclient.presentation.viewmodel.MainViewModel
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.maks_buriak.videoclient.presentation.viewmodel.ServerSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    serverSelectionViewModel: ServerSelectionViewModel,
    onAddPhone: () -> Unit,
    onAddNick: () -> Unit,
    onSignOut: () -> Unit,
    onOpenStream: (String) -> Unit,
) {
    // UI тут, можна звертатись до viewModel

    var showServerSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val context = LocalContext.current

    var menuExpandet by rememberSaveable { mutableStateOf(false) }

    val uiMessage by mainViewModel.uiMessage.collectAsState()

    val currentUser by mainViewModel.currentUser.collectAsState()

    val phoneAction = if (currentUser?.phoneNumber.isNullOrEmpty())
        MainViewModel.PhoneAction.ADD
    else
        mainViewModel.getPhoneAction()

    uiMessage?.let { msg ->
        LaunchedEffect(msg) {
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
            mainViewModel.clearUiMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Client")},
                actions = {
                    IconButton(onClick = { menuExpandet = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                    }

                    DropdownMenu(
                        expanded = menuExpandet,
                        onDismissRequest = { menuExpandet = false }
                    ) {

                        //Верхня частина меню з акаунтом
                        currentUser?.let { account ->
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                //Аватарка
                                AsyncImage(
                                    model = account.photoUrl ?: R.drawable.ic_launcher_background,
                                    contentDescription = "User Avatar",
                                    modifier = Modifier
                                        .size(65.dp)
                                        .clip(CircleShape),
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = account.displayName ?: "Unknown",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = account.nickName ?: "Нік не встановлено",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(7.dp))
                                    Text(
                                        text = account.email ?: "",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = account.phoneNumber ?: "",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            HorizontalDivider()
                        }

                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (phoneAction == MainViewModel.PhoneAction.ADD)
                                        "Додати номер телефону"
                                    else
                                        "Змінити номер телефону"
                                )
                            },
                            onClick = {
                                menuExpandet = false
                                mainViewModel.checkPhoneVerification {
                                    onAddPhone()
                                }
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (mainViewModel.getNickAction() == MainViewModel.NickAction.ADD)
                                        "Додати нік"
                                    else
                                        "Змінити нік"
                                )
                            },
                            onClick = {
                                menuExpandet = false
                                mainViewModel.checkNickChange {
                                    onAddNick()
                                }
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            //три крапки справа вгорі

                            text = { Text("Вийти з акаунту") },
                            onClick = {
                                menuExpandet = false
                                mainViewModel.signOut()

                                onSignOut()
                            }
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showServerSheet = true
                        serverSelectionViewModel.loadServers()
                    }
                ) {
                    Text("Трансляція відео")
                }
            }

            // BottomSheet картка
            if (showServerSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showServerSheet = false },
                    sheetState = sheetState
                ) {
                    // Використовуємо наш ServerSelectionContent
                    ServerSelectionContent(
                        viewModel = serverSelectionViewModel,
                        onServerSelected = { server ->
                            showServerSheet = false
                            onOpenStream(server.address)
                        }
                    )
                }
            }
        }
    )
}