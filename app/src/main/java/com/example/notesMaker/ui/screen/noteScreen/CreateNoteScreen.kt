package com.example.notesMaker.ui.screen.noteScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesMaker.repository.Note
import java.time.Instant.now

@Composable
fun CreateNoteScreen (
    modifier: Modifier  = Modifier,
    viewModel: EditScreenVIewModel = hiltViewModel(),
    onBackPressed : () -> Unit,
    redirectBackToDetailedList : () -> Unit ,
    ) {
    val uiState by  viewModel.uiState.collectAsState()
    var content by rememberSaveable { mutableStateOf("") }
    var heading by rememberSaveable { mutableStateOf("") }

    EditNoteScreen(
        content =content,
        serverResponse = uiState.serverResponse,
        heading = heading,
        onSaveNoteClick = {
            viewModel.createNote(
                Note(
                content = content,
                heading = heading,
                    createdAt = now().toString(),
                )
            )
            redirectBackToDetailedList()
        },
        onBackPressed = onBackPressed,
        onHeadingValueChange ={heading = it},
        onContentValueChange = {content = it } ,
        modifier =  modifier.fillMaxSize()
    )
}