package com.maks_buriak.videoclient.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.maks_buriak.videoclient.presentation.screen.AuthScreen
import com.maks_buriak.videoclient.presentation.screen.CameraStreamScreen
import com.maks_buriak.videoclient.presentation.screen.MainScreen
import com.maks_buriak.videoclient.presentation.screen.NickNameScreen
import com.maks_buriak.videoclient.presentation.screen.PhoneAuthScreen
import com.maks_buriak.videoclient.presentation.viewmodel.AuthViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.MainViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.NickNameViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.PhoneAuthViewModel
import com.maks_buriak.videoclient.presentation.viewmodel.ServerSelectionViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object PhoneAuth : Screen("phone_auth")
    object Messages : Screen("messages")
    object NickName : Screen("nick_name")
    object CameraStream : Screen("camera_stream/{serverUrl}") {
        fun createRoute(serverUrl: String) = "camera_stream/${serverUrl.replace("/", "_")}"
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavHost(navController: NavHostController) {


    val authViewModel: AuthViewModel = koinViewModel()
    val userState by authViewModel.userState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = when {
            userState == null -> Screen.Auth.route
            userState?.nickName.isNullOrEmpty() -> Screen.NickName.route
            else -> Screen.Messages.route
        }
    ) {

        composable(Screen.NickName.route) {
            val viewModel: NickNameViewModel = koinViewModel()
            val uid = userState?.uid ?: return@composable

            NickNameScreen(
                uid = uid,
                viewModel = viewModel,
                onNickSaved = {
                    navController.navigate(Screen.Messages.route) {
                        popUpTo(Screen.NickName.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = authViewModel,
                onSignedIn = {
                    val user = userState
                    if (user?.nickName.isNullOrEmpty()) {
                        navController.navigate(Screen.NickName.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Messages.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Messages.route) {
            val mainViewModel: MainViewModel = koinViewModel()
            val serverSelectionViewModel: ServerSelectionViewModel = koinViewModel()
            MainScreen(
                mainViewModel = mainViewModel,
                serverSelectionViewModel = serverSelectionViewModel,
                onAddPhone = {
                    navController.navigate(Screen.PhoneAuth.route)
                },
                onAddNick = {
                    navController.navigate(Screen.NickName.route)
                },
                onSignOut = {
                    mainViewModel.signOut()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onOpenStream = { serverUrl ->
                    navController.navigate(Screen.CameraStream.createRoute(serverUrl))
                }
            )
        }

        composable(Screen.PhoneAuth.route) {
            val phoneAuthViewModel: PhoneAuthViewModel = koinViewModel()

            PhoneAuthScreen(
                viewModel = phoneAuthViewModel,
                onVerified = {
                    navController.popBackStack() // повертаємось у Messages
                }
            )
        }

        composable(
            route = Screen.CameraStream.route,
            arguments = listOf(navArgument("serverUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val serverUrl = backStackEntry.arguments?.getString("serverUrl")?.replace("_", "/") ?: ""
            CameraStreamScreen(serverUrl = serverUrl)
        }
    }
}