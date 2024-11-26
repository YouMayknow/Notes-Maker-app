package com.example.notesMaker.ui.screen.noteScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesMaker.network.UpdatedShortNote
import java.time.Instant.now

@Composable
fun UpdateNoteScreen(
    modifier: Modifier = Modifier ,
    localNoteId : Int  ,
    onBackPressed : () -> Unit ,
    redirectBackToDetailedList : ()-> Unit ,
    viewModel: UpdateAndEditNoteScreenViewModel = hiltViewModel()
) {
    val uiState by  viewModel.uiState.collectAsState()
    var heading by rememberSaveable { mutableStateOf(uiState.note.heading) }
    var content by rememberSaveable { mutableStateOf(uiState.note.content) }

    LaunchedEffect (Unit){
        viewModel.getNote(localNoteId) // here 1 will be replaced to the note get from the shortNote
    }

    EditNoteScreen(
        content = content,
        serverResponse = uiState.serverResponse,
        heading =  heading,
        onSaveNoteClick = {
            viewModel.updateNote(
                UpdatedShortNote(
                    content = content,
                    heading = heading,
                    id = uiState.note.id ,
                    localNoteId = uiState.note.noteId ,
                    lastUpdated = now().toString() ,
                    version = uiState.note.version + 1
                )
            )
            redirectBackToDetailedList()
        },
        onBackPressed = onBackPressed,
        onHeadingValueChange =  {heading = it},
        onContentValueChange =  {content = it} ,
        modifier =  modifier.fillMaxSize()

    )
}

