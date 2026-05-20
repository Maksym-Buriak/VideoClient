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
import androidx.compose.material3.TextField
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
import com.maks_buriak.videoclient.presentation.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(messageViewModel: MessageViewModel, onAddPhone: () -> Unit, onAddNick: () -> Unit, onSignOut: () -> Unit) {
    // UI тут, можна звертатись до viewModel

    val context = LocalContext.current

    var messageText by rememberSaveable { mutableStateOf("") }
    var menuExpandet by rememberSaveable { mutableStateOf(false) }

    val uiMessage by messageViewModel.uiMessage.collectAsState()

    val currentUser by messageViewModel.currentUser.collectAsState()

    val phoneAction = if (currentUser?.phoneNumber.isNullOrEmpty())
        MessageViewModel.PhoneAction.ADD
    else
        messageViewModel.getPhoneAction()

    uiMessage?.let { msg ->
        LaunchedEffect(msg) {
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
            messageViewModel.clearUiMessage()
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
                                    if (phoneAction == MessageViewModel.PhoneAction.ADD)
                                        "Додати номер телефону"
                                    else
                                        "Змінити номер телефону"
                                )
                            },
                            onClick = {
                                menuExpandet = false
                                messageViewModel.checkPhoneVerification {
                                    onAddPhone()
                                }
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (messageViewModel.getNickAction() == MessageViewModel.NickAction.ADD)
                                        "Додати нік"
                                    else
                                        "Змінити нік"
                                )
                            },
                            onClick = {
                                menuExpandet = false
                                messageViewModel.checkNickChange {
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
                                messageViewModel.signOut()

                                onSignOut()
                            }
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                TextField(
                    value = messageText,
                    onValueChange = {messageText = it},
                    placeholder = { Text("Введіть повідомлення") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        messageViewModel.sendMessage(messageText)
                        messageText = ""
                    }
                ) {
                    Text("Відправити на сервер")
                }
            }
        }
    )
}