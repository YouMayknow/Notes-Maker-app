package com.example.limitlife.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.limitlife.ui.screen.entryScreen.LoginScreen
import com.example.limitlife.ui.screen.entryScreen.SignupScreen
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen (
    navController: NavHostController = rememberNavController() ,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = EntryScreen , modifier = modifier){
        composable<Home> {
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center , modifier =  modifier.fillMaxSize()) {
                Button(onClick = {navController.navigate(Signup) }) {
                }
            }
        }
        navigation<EntryScreen>( startDestination = Signup){
            composable<Login>{
                LoginScreen()
            }
            composable<Signup> {
                SignupScreen(navigateToLoginScreen = { navController.navigate(Login) })
            }
        }

    }
}

@Serializable object EntryScreen
@Serializable  object Login
@Serializable object Signup
@Serializable object Home

