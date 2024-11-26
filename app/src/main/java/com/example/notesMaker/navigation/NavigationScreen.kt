package com.example.notesMaker.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.notesMaker.ui.screen.mainScreen.MainScreen
import com.example.notesMaker.utils.isInternetAvailable


/*it is a main  navigation screen that have access of all the screen except entry screen it takes to notesListAndDetail
screen that have the both our screen .
on this basis of the click it prepares the drawer based screen for the user using individual screen
 */
@Composable
fun  NavigationScreen (
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationScreenViewModel = hiltViewModel(),
) {
    val uiState =  viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
            viewModel.checkLoginRequirement()
    }
    if (uiState.value.isNewUser == null || uiState.value.isTokenValid == null || uiState.value.isNetworkAvailable == null ){
        CircularProgressIndicator()
    }
    else {
        NavHost(
            navController = navController,
            startDestination = when
            {
               uiState.value.isNewUser  == true && uiState.value.isNetworkAvailable == true -> EntryScreen
               uiState.value.isTokenValid == true -> MainScreen
               else -> MainScreen
            } , // here the start destination will differ on the basis of the viewModel need to implement

            modifier = modifier ,
                popEnterTransition = {
                    slideIn(tween(100, easing = LinearOutSlowInEasing)) { fullSize ->
                        // Specifies the starting offset of the slide-in to be 1/4 of the width to the
                        // right,
                        // 100 (pixels) below the content position, which results in a simultaneous slide up
                        // and slide left.
                        IntOffset(fullSize.width / 4, 100)
                    }
                },
        ) {
            nestedMainScreenNavigation(navController = navController)
            nestedAuthNavigation(navController = navController)
        }
    }
}
