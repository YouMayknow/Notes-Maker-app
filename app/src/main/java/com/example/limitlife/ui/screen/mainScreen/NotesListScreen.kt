package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.limitlife.R
import com.example.limitlife.utils.dummyList


@Composable
fun NotesListMainScreen (
    onDetailsIconClicked : () -> Unit ,
    notes: List<Pair<String, String>>,
    onAddNoteClick: () -> Unit,
    modifier : Modifier = Modifier  ,
    isSideBarEnabled :Boolean = false ,
    viewModel: NotesListScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
 ) {
   // val uiState = viewModel.getUserToken.collectAsState()
    Scaffold(
        modifier = modifier ,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        } ,
        topBar = {
            SearchBar(
                onDetailsIconClicked = onDetailsIconClicked,
                isSideBarEnabled =  isSideBarEnabled,
                onSearch =  {}
            )
        }
    ) {
        Text(text =   "fddf") //uiState.value.userToken)
        NotesList(notes = notes , modifier = Modifier.padding(it))
    }
}

@Composable
fun NotesList(notes: List<Pair<String, String>> , modifier: Modifier = Modifier ) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(notes) { note ->
            // Each note will have a title and an image URL
            NoteItem(noteTitle = note.first, imageUrl = note.second , modifier = Modifier.aspectRatio(0.75f))
        }
    }
}
@Composable
fun NoteItem(noteTitle: String, imageUrl: String , modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                IconButton(onClick = { /*TODO*/ }  ) {
                   // Icon(i = vecto(image = R.drawable.baseline_more_vert_24), contentDescription = null  )
                    Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24), contentDescription = null , tint = Color.Black)
                }
            }

        }
    }
}



@Composable
fun SearchBar (
    onDetailsIconClicked : () -> Unit ,
    modifier: Modifier = Modifier ,
    isSideBarEnabled : Boolean = true ,
    onSearch: (String) -> Unit ,
) {
    Row(  modifier = modifier
        .padding(
            top = 12.dp,
            end = 8.dp,
            bottom = 12.dp,
            start = 8.dp
        )
        .statusBarsPadding()
    )  {
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(text = "Search...", fontSize = 16.sp) },
            leadingIcon = {
                IconButton(
                    onClick = onDetailsIconClicked ,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_24), // Replace with your mic icon
                        contentDescription = "Mic",
                        tint = Color.Gray ,
                        modifier =  Modifier.fillMaxSize()
                    )
                }
            },
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
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNoteItem() {
    NoteItem(
        noteTitle = "Unit 7: Part 1",
        imageUrl = "https://example.com/image1.jpg"
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNotesList() {
    val notes =
    NotesListMainScreen(notes = dummyList  , onAddNoteClick =  {} , onDetailsIconClicked = {} )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(onSearch = { } , onDetailsIconClicked = {})
}