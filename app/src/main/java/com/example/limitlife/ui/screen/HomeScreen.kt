package com.example.limitlife.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
    modifier: Modifier = Modifier ,
    navController: NavHostController = rememberNavController() ,
    viewModel: HomeScreenViewModel = hiltViewModel() ,
) {
    Column(
        Modifier.fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        val uiState by viewModel.uiState.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.checkLoginRequirement()
        }
        if ((uiState.isNewUser == null || uiState.isTokenValid == null)  && uiState.isNetworkAvailable == null ){
            CircularProgressIndicator()
        }
        else {
            NavHost(
                navController = navController,
                startDestination = when {
                   uiState.isNewUser == true -> EntryScreen
                    uiState.isTokenValid == true -> MainScreen
                    uiState.isNetworkAvailable == false -> MainScreen
                    else -> EntryScreen
                } ,
                modifier = modifier
            ){
                composable<MainScreen>{
                    MainScreenNavigation(modifier = modifier)
                }
                composable<EntryScreen> {
                    SignupScreen(navigateToMainScreen = {navController.navigate(MainScreen)} , modifier =  modifier )
                }
            }
        }
    }
}
@Serializable object EntryScreen
@Serializable object MainScreen