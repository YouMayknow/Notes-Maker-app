package com.example.limitlife.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.limitlife.ui.screen.entryScreen.SignupScreen
import com.example.limitlife.ui.screen.mainScreen.MainScreenNavigation
import kotlinx.serialization.Serializable


/*This screen have access to all the screen of the app and sub navigation and navigation
is implemented with it
 */
@Composable
fun HomeScreen (
    navController: NavHostController = rememberNavController() ,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = EntryScreen , modifier = modifier){
        composable<MainScreen>{
            MainScreenNavigation()
        }
        composable<EntryScreen> {
            SignupScreen(navigateToMainScreen = {navController.navigate(MainScreen)})
        }

    }
}

@Serializable object EntryScreen
@Serializable object MainScreen