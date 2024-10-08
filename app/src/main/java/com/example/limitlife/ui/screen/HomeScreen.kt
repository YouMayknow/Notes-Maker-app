package com.example.limitlife.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.limitlife.repository.NetworkStatus
import com.example.limitlife.repository.NetworkStatusManager
import com.example.limitlife.ui.screen.entryScreen.SignupScreen
import com.example.limitlife.ui.screen.mainScreen.MainScreenNavigation
import com.example.limitlife.ui.theme.LimitLifeTheme
import kotlinx.serialization.Serializable


/*This screen have access to all the screen of the app and sub navigation and navigation
is implemented with it
 */



@Composable
fun HomeScreen (
    navController: NavHostController = rememberNavController() ,
    modifier: Modifier = Modifier.fillMaxSize() ,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    
    val networkStatus by viewModel.networkStatus.collectAsState()
    val isNewUser = viewModel.isNewUser.collectAsState()
    val isTokenValid = viewModel.isTokenValid.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize() ,
    ) {
        when(networkStatus){
            is NetworkStatus.Connected ->  {
                LaunchedEffect(Unit) {
                    viewModel.checkLoginRequirement()
                }
                if (isNewUser.value == null || isTokenValid.value == null){
                    Column(
                        Modifier.fillMaxSize() ,
                        horizontalAlignment = Alignment.CenterHorizontally ,
                        verticalArrangement = Arrangement.Center

                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    NavHost(
                        navController = navController,
                        startDestination = when {
                            isNewUser.value == true -> EntryScreen
                            isTokenValid.value == true -> MainScreen
                            else -> EntryScreen
                        } ,
                        modifier = modifier
                    ){
                        composable<MainScreen>{
                            MainScreenNavigation(modifier = modifier )
                        }
                        composable<EntryScreen> {
                            SignupScreen(navigateToMainScreen = {navController.navigate(MainScreen)} , modifier =  modifier )
                        }
                    }
                }
            }
            else -> {
                Text(text = "NO internet connection found ")
            }
        }
    }
}

@Serializable object EntryScreen
@Serializable object MainScreen

@Composable
@Preview
fun circularProgress() {
    LimitLifeTheme {
        CircularProgressIndicator()
    }
}