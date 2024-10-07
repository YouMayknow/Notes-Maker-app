package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.limitlife.R
import com.example.limitlife.network.ShortNote
import com.example.limitlife.ui.theme.LimitLifeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    initialText : String ="" ,
    initialHeading :  String = "",
    noteID : Int =   0  ,
    modifier: Modifier = Modifier ,
    navigateToNotesListScreen : () -> Unit ,
    viewModel: EditScreenVIewModel = hiltViewModel()
) {
    val problem = viewModel.response.collectAsState()
    var text by rememberSaveable { mutableStateOf(initialText) }
    var heading by rememberSaveable { mutableStateOf(initialHeading) }
    var  isFocused  by  rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        TopAppBar(
            title = {
                BasicTextField(
                    value = if(heading.isEmpty() && !isFocused) "Create Heading" else heading ,
                    onValueChange = { heading = it },
                    textStyle = TextStyle(
                        fontSize = 18.sp ,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )  ,
                    modifier = modifier.onFocusChanged { focusState -> isFocused = focusState.isFocused } ,

                )
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier.clickable { /* Handle back navigation */ }
                )
            },
            actions = {
                SaveButton {
                        viewModel.createNote(ShortNote(content = text , heading = heading,))
                            viewModel.updateNote(ShortNote(content = text , heading = heading) , noteid = noteID)
                    navigateToNotesListScreen()
                }
            } ,
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
        )
        Text(text = problem.value)
        CustomNoteTextField(
            initialText = initialText ,
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            editedText = {text = it}
        )
    }
}


@Composable
fun SaveButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        // Save Icon
        Icon(
            painter = painterResource(id = R.drawable.baseline_save_24),
            contentDescription = stringResource(id = R.string.save),
        )
        // Save Text
        Text(
            text = stringResource(id = R.string.save),
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun EditScreenTextDecorationBar() {

}

@Composable
fun CustomNoteTextField(
    initialText: String = "",
    modifier: Modifier = Modifier,
    editedText : (String) -> Unit ,
    placeholder: String = "Enter your note here...",
) {
    var text by remember { mutableStateOf(initialText) }
    var isFocused by remember { mutableStateOf(false) }
    var undoStack = remember { mutableListOf<String>() }
    var redoStack = remember { mutableListOf<String>() }

    // Track formatting states (bold, italic, underline)
    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }
    var isUnderlined by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16.sp) }

    // Border and background color change on focus
    val borderColor = if (isFocused) Color.Blue else Color.Gray
    val backgroundColor = if (isFocused) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, shape = RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            if (text.isEmpty() && !isFocused) {
                // Placeholder Text
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = fontSize,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
            BasicTextField(
                value = text,
                onValueChange = {
                    undoStack.add(text)  // Add previous text state to the undo stack
                    text = it
                    editedText(it)
                },
                textStyle = TextStyle(
                    fontSize = fontSize,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (isUnderlined) TextDecoration.Underline else null,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState -> isFocused = focusState.isFocused }
                    .padding(end = 32.dp, start = 8.dp, top = 8.dp, bottom = 8.dp), // Padding to make space for clear button
                maxLines = 10 // Max number of lines
            )
            // Clear Text Button
            if (text.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    contentDescription = "Clear text",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { text = "" }
                        .padding(4.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { isBold = !isBold }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_format_bold_24),
                    contentDescription = "Bold",
                    tint = if (isBold) Color.Blue else Color.Black
                )
            }
            IconButton(onClick = { isItalic = !isItalic }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_format_italic_24),
                    contentDescription = "Italic",
                    tint = if (isItalic) Color.Blue else Color.Black
                )
            }
            IconButton(onClick = { isUnderlined = !isUnderlined }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_format_underlined_24),
                    contentDescription = "Underline",
                    tint = if (isUnderlined) Color.Blue else Color.Black
                )
            }
            IconButton(onClick = {
                fontSize.value.inc() // Increase text size
                fontSize.value.inc() // Increase text size
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_format_size_24),
                    contentDescription = "Increase font size"
                )
            }
            IconButton(onClick = {
                if (undoStack.isNotEmpty()) {
                    redoStack.add(text)
                    text = undoStack.removeLast()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_undo_24),
                    contentDescription = "Undo"
                )
            }
            IconButton(onClick = {
                if (redoStack.isNotEmpty()) {
                    undoStack.add(text)
                    text = redoStack.removeLast()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_undo_24),
                    contentDescription = "Redo"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomNoteTextField() {
    CustomNoteTextField(editedText = {  })
}


@Preview(showBackground = true)
@Composable
fun PreviewSaveButton() {
    SaveButton(onClick = { /* Handle save */ })
}

@Preview(showBackground = true)
@Composable
fun PreviewEditNoteScreen() {
    LimitLifeTheme {
        RouteEditNoteScreen()
    }
}
