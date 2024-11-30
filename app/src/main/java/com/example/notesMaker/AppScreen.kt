package com.example.notesMaker

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.example.notesMaker.ui.screen.HomeScreen
import com.example.notesMaker.ui.screen.entryScreen.LoginScreen
import com.example.notesMaker.ui.theme.NotesMakerTheme

@Composable
fun AppScreen(
    modifier: Modifier = Modifier.padding(
        start = WindowInsets.safeDrawing
            .asPaddingValues()
            .calculateStartPadding(LayoutDirection.Ltr) ,
        end = WindowInsets.safeDrawing
            .asPaddingValues()
            .calculateEndPadding(layoutDirection = LayoutDirection.Ltr )
    )
) {
   HomeScreen()
}