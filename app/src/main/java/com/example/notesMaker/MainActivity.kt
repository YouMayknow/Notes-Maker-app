package com.example.notesMaker

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notesMaker.ui.screen.entryScreen.LoginScreen
import com.example.notesMaker.ui.theme.NotesMakerTheme
import com.example.notesMaker.utils.askForNotification
import com.example.notesMaker.utils.basicNotificationFramework
import com.example.notesMaker.utils.createNotificationChannel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            createNotificationChannel(this)
            askForNotification(this)
        }
        enableEdgeToEdge()
        setContent {
            NotesMakerTheme {
             Scaffold {
                 AppScreen(modifier = Modifier.padding(it))
             }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesMakerTheme {
        Greeting("Android")
    }
}