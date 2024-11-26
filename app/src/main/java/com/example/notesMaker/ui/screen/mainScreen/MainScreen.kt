package com.example.notesMaker.ui.screen.mainScreen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesMaker.ui.theme.NotesMakerTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/* It is a screen that contains the modal navigation  drawer and the list
main Screen that adhere the notes from the database and also have button to add the notes
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesListScreenViewModel = hiltViewModel(),
    onNoteClick: (Int) -> Unit,
    onAddNoteClick: () -> Unit,
    onDrawerItemClicked: (String) -> Unit,
    onNotificationsIconClicked: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            DrawerScreen(onDrawerItemClicked = onDrawerItemClicked)
        } ,
        drawerState = drawerState ,
        modifier =  modifier ,
    ) {
        NotesListScreen(
            modifier = modifier,
            onNoteClick = onNoteClick,
            onDetailsIconClicked = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
            onAddNoteClick = onAddNoteClick,
            onNotificationsIconClicked = onNotificationsIconClicked,
            viewModel = viewModel
        )
    }
}

@Preview
@Composable
fun NavigationRailPreview() {
    NotesMakerTheme(darkTheme = false) {
        MainScreen(
            viewModel = hiltViewModel(),
            onNoteClick =  {},
            onAddNoteClick = {},
            onDrawerItemClicked =  {}
        ) {}
    }
}
