package com.unitt.unitt.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unitt.unitt.features.auth.AuthFlow
import com.unitt.unitt.features.auth.AuthViewModel
import com.unitt.unitt.features.main.UserPrototypeApp

private object AppRoute {
    const val Auth = "auth"
    const val Main = "main"
}

@Composable
fun UniTTApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoute.Auth) {
        composable(AppRoute.Auth) {
            val authViewModel: AuthViewModel = viewModel()
            AuthFlow(authViewModel) {
                navController.navigate(AppRoute.Main) {
                    popUpTo(AppRoute.Auth) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable(AppRoute.Main) {
            UserPrototypeApp()
        }
    }
}
