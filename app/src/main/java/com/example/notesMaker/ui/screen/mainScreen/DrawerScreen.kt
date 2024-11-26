package com.example.notesMaker.ui.screen.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesMaker.ui.theme.NotesMakerTheme
import com.example.notesMaker.utils.DrawerItems


/* this screen makes the drawer screen on the basis of
 options clicked by the user
 */

@Composable
fun DrawerScreen (
    modifier: Modifier = Modifier ,
    onDrawerItemClicked : (String) -> Unit  ,
) {
    ModalDrawerSheet {
        Text(
            text = "Notes Maker", // The app name
            style = MaterialTheme.typography.displayMedium  ,   // Set larger font size for the title
            fontWeight = FontWeight.Bold,  // Set the text as bold
            color = MaterialTheme.colorScheme.onBackground ,// Using the theme color
            modifier =  modifier.padding(start = 4.dp , top = 12.dp , bottom = 8.dp)
        )
        HorizontalDivider(modifier = Modifier.height(2.dp))
        MenuItem(icon = Icons.Default.AccessTime, text = DrawerItems.Recent.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.Star, text = DrawerItems.Starred.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.OfflinePin, text = DrawerItems.Offline.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.Default.Delete, text = DrawerItems.Bin.name, onClick =onDrawerItemClicked)
        HorizontalDivider(modifier = Modifier.height(2.dp))
        MenuItem(icon = Icons.Default.Settings, text = DrawerItems.Settings.name, onClick = onDrawerItemClicked)
        MenuItem(icon = Icons.AutoMirrored.Filled.Help, text = DrawerItems.HelpAndFeedback.name, onClick =onDrawerItemClicked)
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


@Composable
fun IndividualDrawerScreen (
    onBackClick : () -> Unit , 
    modifier: Modifier = Modifier ,
    drawerItemName : String , 

) {
    BackHandler {
        onBackClick()
    }
    Scaffold(
        modifier = modifier.fillMaxSize() ,
        topBar = {
            Row(
                modifier
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                ,
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.SpaceAround ,


            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null )
                }
               Text(
                   text = drawerItemName ,
                   style = MaterialTheme.typography.headlineMedium ,
                   fontStyle = FontStyle.Normal ,
                   fontWeight = FontWeight.SemiBold
               )
                Spacer(modifier = modifier.weight(1f))
            }
        }
    ) {
        Text(text = "asdfsd", modifier.padding(it))
      //  NotesList(notes = dummyList , modifier = modifier.padding(it))
    }
}


@Composable
@Preview
fun IndividualDrawerScreenPreview () {
    NotesMakerTheme {

    IndividualDrawerScreen(onBackClick = { /*TODO*/ }, drawerItemName = DrawerItems.Bin.name )
    }
}


@Preview(showBackground = true)
@Composable
fun SidebarMenuPreview() {
    NotesMakerTheme {
        DrawerScreen(onDrawerItemClicked =  {})
    }
}
