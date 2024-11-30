package com.example.notesMaker.ui.screen.noteScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    LaunchedEffect (Unit){
        viewModel.getNote(localNoteId) // here 1 will be replaced to the note get from the shortNote
    }

    EditNoteScreen(
        content = uiState.note.content,
        serverResponse = uiState.serverResponse,
        heading =  uiState.note.heading,
        onSaveNoteClick = {
            viewModel.updateNote(
                UpdatedShortNote(
                    content = uiState.note.content,
                    heading = uiState.note.heading,
                    id = uiState.note.noteId ?: -1,
                    localNoteId = uiState.note.localNoteId ,
                    lastUpdated = now().toString() ,
                    version = uiState.note.version + 1
                )
            )
            redirectBackToDetailedList()
        },
        onBackPressed = onBackPressed,
        onHeadingValueChange =  viewModel::onNoteHeadingChange,
        onContentValueChange =  viewModel::onNoteContentChange ,
        modifier =  modifier.fillMaxSize()
    )
}

  