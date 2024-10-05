import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.limitlife.R
import com.example.limitlife.ui.theme.LimitLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen() {
    var text by remember { mutableStateOf("enter content") }
    var heading by remember { mutableStateOf("choose heading") }

    Column {
        // Top Bar with back button and heading
        TopAppBar(
            title = {
                BasicTextField(
                    value = heading,
                    onValueChange = { heading = it },
                    textStyle = TextStyle(fontSize = 18.sp)
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
                // Save icon
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = stringResource(id = R.string.save),
                    modifier = Modifier.clickable { /* Handle save action */ }
                )
            }
        )

        // Content input
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = TextStyle(fontSize = 16.sp)
            )
        }

        // Bottom action bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_undo_24),
                contentDescription = stringResource(id = R.string.undo),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* Handle undo */ }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_undo_24),
                contentDescription = stringResource(id = R.string.redo),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* Handle redo */ }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_bold_24),
                contentDescription = stringResource(id = R.string.bold),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* Handle bold */ }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_size_24),
                contentDescription = stringResource(id = R.string.text_size),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* Handle text size */ }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_palette_24),
                contentDescription = stringResource(id = R.string.change_color),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* Handle color change */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditNoteScreen() {
    LimitLifeTheme {
        EditNoteScreen()
    }
}
