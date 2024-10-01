package com.example.limitlife.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.limitlife.ui.screen.entryScreen.LoginScreen
import com.example.limitlife.ui.screen.entryScreen.SignupScreen
import com.example.limitlife.ui.screen.mainScreen.MainScreenNavigation
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen (
    navController: NavHostController = rememberNavController() ,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = EntryScreen , modifier = modifier){
        composable<MainScreen>{
            MainScreenNavigation(navController = navController)
        }
        navigation<EntryScreen>( startDestination = Signup){
            composable<Login>{
                LoginScreen(navigateToSignupScreen = {navController.navigate(Signup)})
            }
            composable<Signup> {
                SignupScreen()
            }
        }

    }
}

@Serializable object EntryScreen
@Serializable  object Login
@Serializable object Signup
@Serializable object MainScreen