package com.example.notesMaker.ui.screen.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier ,
    onBackClick : () -> Unit ,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {
    val uiState =  viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.checkForUnSyncedNotes()
    }
    Scaffold(
        topBar = {
            Row(
                modifier = modifier
                    .padding(
                        top = 12.dp,
                        end = 8.dp,
                        start = 8.dp,
                        bottom = 8.dp,
                    ).statusBarsPadding(), // Add status bar padding
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null )
                }
                Text(
                    text = "Notifications" ,
                    style = MaterialTheme.typography.headlineMedium ,
                    fontStyle = FontStyle.Normal ,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = modifier.weight(1f))
            }
        }
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            if (uiState.value.isSyncing == null || uiState.value.isSyncing == true) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).padding(it))
            }
            else {
                LazyColumn(
                    modifier = modifier.padding(it).fillMaxSize()
                ) {
                    items(uiState.value.notifications) { notification ->
                        Column {
                            NotificationCard(notification = notification)
                        }

                    }
                }
            }
        }


    }
}

@Composable
fun NotificationCard(
    modifier: Modifier = Modifier ,
    notification: Notification ,
) {
    Column(modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)) {
       Column {
           Row(modifier = Modifier.fillMaxWidth()) {
               Column(modifier = Modifier.weight(1f)) {
                   Text(text = notification.heading , style = MaterialTheme.typography.bodyMedium)
                   Text(text = notification.body , style = MaterialTheme.typography.bodySmall)
               }
               Column {
                   IconButton(onClick = notification.action) {
                       Icon(imageVector = notification.image, contentDescription = null)
                   }
               }
           }
       }
        Column { Text(text = notification.time , style = MaterialTheme.typography.bodySmall) }
        HorizontalDivider(modifier = Modifier.height(2.dp))
    }

}

class Notification(
     val heading : String,
      val body : String,
     val image: ImageVector ,
     val time : String,
    val action : () -> Unit,
)
val listOfNotifications : List<Notification>  = emptyList()


@Composable
@Preview
fun NotificationCardPreview () {
    NotificationCard(notification = Notification("Heading", "Body",Icons.Default.AccessTime , "12:45", {}))
}