package com.example.notesMaker.ui.screen.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesMaker.R
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.Note
import com.example.notesMaker.ui.screen.noteScreen.DetailedScreen
import com.example.notesMaker.ui.theme.NotesMakerTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen (
    modifier: Modifier = Modifier,
    onNoteClick: (Int) -> Unit,
    onDetailsIconClicked: () -> Unit,
    onAddNoteClick: () -> Unit,
    onNotificationsIconClicked: () -> Unit,
    viewModel: NotesListScreenViewModel
) {
    val query by viewModel.query.collectAsState()
    val notes by viewModel.notes.collectAsState(emptyList())
    val searchResults  by viewModel.searchResults.collectAsState(emptyList())
    val uiState by  viewModel.uiState.collectAsState()
    val searchUiState by  viewModel.searchUiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState()}
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        // here comes the attempt of syncing  the data with the server
        viewModel.snackBarMessage.collect{ message ->
            scope.launch{
                snackBarHostState.showSnackbar(message)
            }
        }
    }
    BackHandler(enabled = query.isNotEmpty()) {
        viewModel.clearSearchWord()
    }

    Scaffold(
        modifier = modifier ,
        floatingActionButton = { FloatingActionButton(onClick = onAddNoteClick) { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note") }
        } ,
        topBar = {
            SearchBar(
                onDetailsIconClicked = onDetailsIconClicked,
                onSearch =  {},
                onNotificationsIconClicked =onNotificationsIconClicked ,
                onSearchValueChanged = viewModel::updateSearchWord,
                searchValue =  searchUiState.searchWord
            )
        } ,
        snackbarHost = {SnackbarHost(hostState = snackBarHostState) } ,
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading ,
            onRefresh = {
                viewModel.syncNotes()
                        },
            state =pullToRefreshState ,
            modifier = Modifier
                .fillMaxSize() ,
            ){
            Box(modifier = Modifier.padding(it)
            ){
                NotesList(
                    notes =   notes ,
                    onNoteClick =  onNoteClick ,
                    onDetailsIconClicked = {noteId ->
                        viewModel.getDetailsOfNote(noteId)
                    } ,
                    onDeleteIconClicked = {noteId , localNoteId ->
                        viewModel.deleteNote(noteId , localNoteId)
                    },
                    onBackupIconClicked ={},
                    onShareIconClicked = {   }
                )
                NotesSearchScreen(
                    onNoteClick= onNoteClick ,
                    listOfSearchOutcomes = searchResults
                )
              if (uiState.isLoading){
                  CircularProgressIndicator(modifier = modifier
                      .align(Alignment.TopCenter)
                      .size(24.dp)
                      .padding(top = 12.dp) , strokeWidth = 3.dp)
              }
                AnimatedVisibility(visible = uiState.isDetailedNoteVisible ) {// add animation here
                    DetailedScreen(
                        modifier
                            .fillMaxSize()
                            .align(Alignment.Center) ,
                        onEdit = {},
                        onClose = viewModel::closeDetailedNote,
                        createdOn  = uiState.detailedNote?.dateCreated?: "",
                        lastEdited =  uiState.detailedNote?.lastCreated?:""
                    )
                }
            }
        }
    }
}
@Composable
fun NotesList(
    modifier: Modifier = Modifier  ,
    notes:List<UpdatedShortNote>,
    onNoteClick : (Int) -> Unit ,
    onDetailsIconClicked: (Int) -> Unit ,
    onDeleteIconClicked: (Int , Int) -> Unit ,
    onBackupIconClicked: (Int) -> Unit ,
    onShareIconClicked : (Int) -> Unit ,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(notes) { note ->
            Box {
                NoteCard(
                    modifier = Modifier.aspectRatio(0.75f),
                    onDetailsIconClicked = { onDetailsIconClicked(note.id)},
                    onDeleteIconClicked = {onDeleteIconClicked(note.id  ,note.localNoteId ?: -1)},
                    onBackupIconClicked = {onBackupIconClicked(note.id)},
                    onShareIconClicked = {onShareIconClicked(note.id)},
                    noteTitle = note.heading,
                    imageUrl = note.content,
                    onNoteClick = { onNoteClick(note.localNoteId ?: -1) }
                )
            }
        }
    }
}
@Composable
fun NoteCard(
    modifier: Modifier = Modifier ,
    onDetailsIconClicked: () -> Unit ,
    onDeleteIconClicked: () -> Unit ,
    onBackupIconClicked: () -> Unit ,
    onShareIconClicked : () -> Unit ,
    noteTitle: String,
    imageUrl: String ,
    onNoteClick : () -> Unit
) {
    var expanded by remember {mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNoteClick() },
        elevation =CardDefaults.cardElevation(12.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Image on the left side of the card
            Image(
                painter = painterResource(id = R.drawable.ic_istagram), // Coil for image loading
                contentDescription = "Note Image",
                modifier = Modifier
                    .aspectRatio(1f)  // Adjust size as necessary
                    .clip(RoundedCornerShape(8.dp))  // Rounded corners for the image
                    .background(Color.Gray),  // Gray background for placeholder or error
                contentScale = ContentScale.Crop  // Crop the image to fill the space
            )

            Spacer(modifier = Modifier.width(16.dp))
            Row(modifier =  Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = noteTitle,
                    modifier = Modifier.weight(1f) ,
                    fontSize =  20.sp,
                    maxLines =  2 ,
                      style = MaterialTheme.typography.titleMedium // here the real is h6
                )

                IconButton(onClick = { expanded =! expanded } ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24), contentDescription = null , tint = Color.Black)
                }
                DropdownMenu(
                    expanded = expanded ,
                    onDismissRequest = { expanded = false }, // Dismiss when clicking outside
                    //offset = DpOffset(4.dp, 4.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Details" , style = MaterialTheme.typography.titleMedium) },
                        onClick = {
                            onDetailsIconClicked()
                            expanded = false
                            // Handle Edit action here
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", style = MaterialTheme.typography.titleMedium) },
                        onClick = {
                            onDeleteIconClicked()
                            expanded = false
                            // Handle Delete action here
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share" , style = MaterialTheme.typography.titleMedium) },
                        onClick = {
                            onShareIconClicked()
                            expanded = false
                            // Handle Share action here
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Offline" , style = MaterialTheme.typography.titleMedium) },
                        onClick = {
                            onBackupIconClicked()
                            expanded = false
                            // Handle Share action here
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun SearchBar (
    searchValue : String ,
    onSearchValueChanged : (String) -> Unit ,
    onNotificationsIconClicked : () -> Unit ,
    onDetailsIconClicked : () -> Unit ,
    modifier: Modifier = Modifier ,
    onSearch: (String) -> Unit ,
) {

    Row(  modifier = modifier
        .padding(
            top = 12.dp,
            end = 8.dp,
            start = 8.dp,
            bottom = 8.dp,
        )
        .statusBarsPadding() , // Add status bar padding
    horizontalArrangement = Arrangement.Center ,
    verticalAlignment = Alignment.CenterVertically
    )  {
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        val keyboardController = LocalSoftwareKeyboardController.current
        IconButton(
            onClick = onDetailsIconClicked ,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_menu_24), // Replace with your mic icon
                contentDescription = "Mic",
                tint = Color.Gray ,
                modifier =  Modifier.fillMaxSize() ,
            )
        }
        TextField(
            value = searchValue  ,
            onValueChange = onSearchValueChanged,
            placeholder = { Text(text = "Search...", fontSize = 16.sp) },
            trailingIcon = {
                IconButton(onClick = {
                    onSearch(searchQuery.text) // Trigger search when the icon is clicked
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24), // Replace with your search icon
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp), // Rounded corners
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // IME action set to "Search"
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchQuery.text) // Trigger search when the "Search" key is pressed
                    keyboardController?.hide() // Hide the keyboard
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor =  Color.Transparent ,
                unfocusedIndicatorColor =  Color.Transparent ,
            ),
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
        )
        IconButton(
            onClick = onNotificationsIconClicked ,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_notifications_24), // Replace with your mic icon
                contentDescription = "Notifications",
                tint = Color.Gray ,
                modifier =  Modifier.fillMaxSize() ,
            )
        }
    }
}

@Composable
fun NotesSearchScreen(
    modifier: Modifier =  Modifier,
    listOfSearchOutcomes  : List<Note>,
    onNoteClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(listOfSearchOutcomes , {it.id}){ note ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = {
                       val note = UpdatedShortNote(
                            heading = note.heading,
                            content = note.content,
                            id = note.id,
                            version = note.version ,
                            localNoteId = note.id ,
                           lastUpdated = note.lastUpdated
                       )
                        onNoteClick(note.localNoteId ?: -1)
                    })
                    .padding(horizontal = 4.dp, vertical = 2.dp) ,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("${note.heading} : ", fontWeight = FontWeight.SemiBold , style = MaterialTheme.typography.bodyMedium , maxLines = 1)
                Text(note.content, style = MaterialTheme.typography.bodySmall , maxLines = 1 )
            }
            HorizontalDivider()
        }
    }
}

@Composable
@Preview
fun NotesSearchScreenPreview() {
    NotesSearchScreen(
//        listOfSearchOutcomes = listOf(
//            UpdatedShortNote(id = 1, heading = "Note 1", content = "Content of Note 1" ),
//            UpdatedShortNote(id = 2, heading = "Note 2", content = "Content of Note 2"),
//            UpdatedShortNote(id = 3, heading = "Note 3", content = "Content of Note 3")
//        )
      listOfSearchOutcomes =   emptyList(),
        onNoteClick = {}
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewNoteItem() {
    NoteCard(
        onDetailsIconClicked = {},
        onDeleteIconClicked = {},
        onBackupIconClicked = {},
        onShareIconClicked = {},
        noteTitle = "Unit 7: Part 1",
        imageUrl = "https://example.com/image1.jpg",
        onNoteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNotesList() {
    NotesListScreen(
        onNoteClick =  {},
        onDetailsIconClicked = {},
        onAddNoteClick =  {},
        onNotificationsIconClicked = {},
        viewModel = hiltViewModel()
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    var  header = ""
    SearchBar(onSearch = { } , onDetailsIconClicked = {} , onNotificationsIconClicked = {} , onSearchValueChanged = { header = it } , searchValue = "header" )
}

@Preview
@Composable
fun NotesListPreview() {
    NotesMakerTheme(darkTheme =  false) {
        NotesList(
            notes = listOf(UpdatedShortNote("hero is the best ", "he is the badass person in the group ", 1)),
            onNoteClick = {},
            onDetailsIconClicked = {},
            onDeleteIconClicked = { _ ,_  -> },
            onBackupIconClicked = {},
            onShareIconClicked = {}
        )
    }
}
@Composable
@Preview
fun CircularProgressBarPreview() {
    NotesMakerTheme {
    CircularProgressIndicator(modifier = Modifier
        .size(24.dp)
        .padding(top = 8.dp) , strokeWidth = 2.dp)
    }
}