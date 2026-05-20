package com.maks_buriak.videoclient.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.maks_buriak.videoclient.presentation.navigation.AppNavHost
import com.maks_buriak.videoclient.ui.theme.VideoClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoClientTheme {
                AppNavHost(navController = rememberNavController())
            }
        }
    }
}