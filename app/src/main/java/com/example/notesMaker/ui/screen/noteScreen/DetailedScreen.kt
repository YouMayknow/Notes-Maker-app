package com.example.notesMaker.ui.screen.noteScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesMaker.R
import com.example.notesMaker.ui.theme.NotesMakerTheme

@Composable
fun DetailedScreen(
    modifier: Modifier = Modifier ,
    onEdit : () -> Unit ,
    onClose : () -> Unit ,
    createdOn : String ,
    lastEdited : String ,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .padding(16.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(16.dp) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.note_details_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Created On Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.created_on_label),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = stringResource(R.string.created_date , createdOn))
            }

            // Last Edited Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.last_edited_on_label),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = stringResource(R.string.last_edited_date ,lastEdited ))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onEdit,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = stringResource(R.string.edit_note_button))
                }
                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(text = stringResource(R.string.close_button))
                }
            }
        }
    }
}
@Composable
@Preview
fun DetailedScreenViewModel() {
    NotesMakerTheme(darkTheme = true) {
        DetailedScreen(onEdit = { /*TODO*/ } , onClose =  {} , createdOn = "fdads0" , lastEdited =  "adfadf" )
    }
}
