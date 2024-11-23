package com.example.notesMaker.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.notesMaker.ui.screen.entryScreen.SignupScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.nestedAuthNavigation (navController : NavHostController) {
    navigation<EntryScreen>(SignUpScreen) {
        composable<SignUpScreen> {
            SignupScreen(navigateToMainScreen = { navController.navigate(MainScreen) })
        }
    }
}
@Serializable object EntryScreen
@Serializable object SignUpScreen
@Serializable object MainScreen