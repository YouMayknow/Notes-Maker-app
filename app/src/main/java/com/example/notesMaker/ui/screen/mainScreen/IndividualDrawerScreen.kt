package com.example.notesMaker.ui.screen.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null )
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

