package com.example.notesMaker.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.notesMaker.ui.screen.entryScreen.SignupScreenOld
import kotlinx.serialization.Serializable

fun NavGraphBuilder.nestedAuthNavigation (navController : NavHostController) {
    navigation<EntryScreen>(SignUpScreen) {
        composable<SignUpScreen> {
            SignupScreenOld(navigateToMainScreen = { navController.navigate(MainScreen) })
        }
    }
}
@Serializable   object EntryScreen
@Serializable  object SignUpScreen
@Serializable  object MainScreen
@Serializable private object ForgotPasswordScreen
@Serializable private object ResetPasswordScreen
@Serializable private object LoginScreen
