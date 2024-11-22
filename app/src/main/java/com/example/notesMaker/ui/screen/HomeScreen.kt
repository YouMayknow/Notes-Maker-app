package com.example.notesMaker.ui.screen

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
import com.example.notesMaker.ui.screen.entryScreen.SignupScreen
import com.example.notesMaker.ui.screen.mainScreen.EntryScreen
import com.example.notesMaker.ui.screen.mainScreen.MainScreen
import com.example.notesMaker.ui.screen.mainScreen.MainScreenNavigation
import kotlinx.serialization.Serializable


/*This screen have access to all the screen of the app and sub navigation and navigation
is implemented with it
 */


@Composable
fun HomeScreen (
    modifier: Modifier = Modifier ,
) {
    Column(
        modifier.fillMaxSize(),
    ) {
        MainScreenNavigation()
    }
}
