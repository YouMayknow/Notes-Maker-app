package com.example.limitlife.ui.screen.NoteScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.limitlife.network.UpdatedShortNote
import kotlinx.serialization.json.Json

@Composable
fun UpdateNoteScreen(
    modifier: Modifier = Modifier ,
    shortNote: String ,
    onBackPressed : () -> Unit ,
    redirectBackToDetailedList : ()-> Unit ,
    viewModel: EditScreenVIewModel = hiltViewModel()
) {
    val uiState by  viewModel.uiState.collectAsState()
        var content by rememberSaveable { mutableStateOf("") }
    var id by rememberSaveable { mutableIntStateOf(-1) }
    var heading by rememberSaveable { mutableStateOf("") }
    LaunchedEffect (Unit){
        val note = Json.decodeFromString<UpdatedShortNote>(shortNote)
        content = note.content
        heading = note.heading
        id = note.id
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
                    id = id,
                    localNoteId = 0
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

