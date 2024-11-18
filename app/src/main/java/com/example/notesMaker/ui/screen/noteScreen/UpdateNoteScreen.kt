package com.example.notesMaker.ui.screen.noteScreen

import android.R.attr.version
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
import com.example.notesMaker.network.UpdatedShortNote
import kotlinx.serialization.json.Json
import java.time.Instant.now

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
    var version by rememberSaveable { mutableStateOf(1) }
    var localNoteId  by rememberSaveable { mutableIntStateOf(-1) }
    LaunchedEffect (Unit){
        val note = Json.decodeFromString<UpdatedShortNote>(shortNote)
        content = note.content
        heading = note.heading
        id = note.id
        localNoteId = note.localNoteId ?: -1
        version = note.version
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
                    id = id ,
                    localNoteId = localNoteId ,
                    lastUpdated = now().toString() ,
                    version = version + 1
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

