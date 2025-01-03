package com.example.notesMaker.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.notesMaker.navigation.NavigationScreen
import com.example.notesMaker.ui.screen.entryScreen.ForgotPasswordScreen
import com.example.notesMaker.ui.screen.entryScreen.LoginScreen
import com.example.notesMaker.ui.screen.entryScreen.SignupScreen


/*This screen have access to all the screen of the app and sub navigation and navigation
is implemented with it
 */


@Composable
fun HomeScreen (
    modifier: Modifier = Modifier ,
) {
//    ForgotPasswordScreen(modifier, {}){}
    SignupScreen({} , {})
}
