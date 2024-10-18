package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.limitlife.ui.theme.LimitLifeTheme
import com.example.limitlife.utils.DrawerItems
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/* It is a screen that contains the modal navigation  drawer and the list
main Screen that adhere the notes from the database and also have button to add the notes
 */
@Composable
fun UserDetailsAndDrawerScreen(
     shouldRefresh : Boolean = false ,
    viewModel: NotesListScreenViewModel ,
    onNoteClick : (String) -> Unit,
    onAddNoteClick :()-> Unit,
     turnShouldRefreshFalse : () -> Unit,
    onDrawerItemClicked: (String) -> Unit,
    modifier : Modifier =   Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerItems(onDrawerItemClicked = onDrawerItemClicked)
        } ,
        drawerState = drawerState ,
        modifier =  modifier ,
    ) {
        NotesListMainScreen(
            modifier = modifier,
            shouldRefresh = shouldRefresh,
            onNoteClick = {
                        val note =  Json.encodeToString(it)
                onNoteClick(note)
                          },
            onDetailsIconClicked = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
            onAddNoteClick = onAddNoteClick,
            viewModel = viewModel ,
            turnShouldRefreshFalse = turnShouldRefreshFalse ,
        )
    }
}

@Composable
fun NavigationDrawerItems (
    modifier: Modifier = Modifier ,
     onDrawerItemClicked : (String) -> Unit  ,
) {
    ModalDrawerSheet {
        Text(
            text = "Limit Life", // The app name
            style = MaterialTheme.typography.displayMedium  ,   // Set larger font size for the title
            fontWeight = FontWeight.Bold,  // Set the text as bold
            color = MaterialTheme.colorScheme.onBackground ,// Using the theme color
            modifier =  modifier.padding(start = 4.dp , top = 12.dp , bottom = 8.dp)
        )
        Divider(modifier = Modifier.height(2.dp))
        MenuItem(icon = Icons.Default.AccessTime, text = DrawerItems.Recent.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.Star, text = DrawerItems.Starred.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.OfflinePin, text = DrawerItems.Offline.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.Delete, text = DrawerItems.Bin.name, onClick =onDrawerItemClicked)
        Divider(modifier = Modifier.height(2.dp))
        MenuItem(icon = Icons.Default.Settings, text = DrawerItems.Settings.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.Help, text = DrawerItems.HelpAndFeedback.name, onClick =onDrawerItemClicked)
    }
}

@Composable
fun MenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String , onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp)
            .clickable { onClick(text) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text , style = MaterialTheme.typography.headlineSmall )
    }
}



@Preview
@Composable
fun NavigationRailPreview() {
    LimitLifeTheme(darkTheme = false) {
        UserDetailsAndDrawerScreen(
            viewModel = hiltViewModel(),
            onNoteClick =  {},
            onAddNoteClick = {}, turnShouldRefreshFalse = {}, onDrawerItemClicked =  {})
    }
}


@Preview(showBackground = true)
@Composable
fun SidebarMenuPreview() {
    LimitLifeTheme {
        NavigationDrawerItems(onDrawerItemClicked =  {})
    }
}