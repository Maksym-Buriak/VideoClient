package com.maks_buriak.videoclient.presentation.screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.maks_buriak.videoclient.presentation.viewmodel.PhoneAuthViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun PhoneAuthScreen(
    viewModel: PhoneAuthViewModel,
    onVerified: () -> Unit
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    val codeSent by viewModel.codeSent.collectAsState()

    val status by viewModel.status.collectAsState()

    val context = LocalContext.current
    val currentActivityProvider = { context as Activity }

    val isVerified by viewModel.isVerified.collectAsState()
    val isSending by viewModel.isSending.collectAsState()

    LaunchedEffect(isVerified) {
        if (isVerified) {
            onVerified()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Підтвердження номеру телефону", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (!codeSent) {
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Номер телефону") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.sendCode(phoneNumber, currentActivityProvider)
                },
                enabled = phoneNumber.isNotBlank() && !isSending,
            ) {
                Text("Надіслати код")
            }
        } else {

            TextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Введіть код") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.verifyCode(code, phoneNumber)
                },
                enabled = code.isNotBlank() && !isSending
            ) {
                Text("Підтвердити код")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        status?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    }
}