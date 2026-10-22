package com.yaryadom.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yaryadom.app.ui.screens.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val HOME = "home"
    const val SEARCH = "search"
    const val LISTING_DETAILS = "listing/{id}"
    const val PROFILE = "profile"
}

@Composable
fun YaRyadomNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    // TODO: проверить авторизацию и перейти на AUTH или HOME
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.AUTH) {
            // TODO: AuthScreen из feature:auth
        }

        composable(Routes.HOME) {
            // TODO: HomeScreen
        }

        // Остальные маршруты будут добавлены по мере реализации feature-модулей
    }
}
