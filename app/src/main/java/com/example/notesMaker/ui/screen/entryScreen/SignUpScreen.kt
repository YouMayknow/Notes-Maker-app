package com.example.notesMaker.ui.screen.entryScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesMaker.R
import com.example.notesMaker.ui.theme.NotesMakerTheme


@Composable
fun SignupScreen(modifier : Modifier = Modifier) {
    val scrollState = rememberScrollState()
    var  name   =  rememberSaveable{mutableStateOf("")}
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center ,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.and_start_taking_notes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(24.dp))

            TextFieldLabel(R.string.full_name)
            OutlinedTextField(
                shape = MaterialTheme.shapes.small,
                value =name.value,
                onValueChange = {name.value = it },
                placeholder = { Text("Example: John Doe") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextFieldLabel(R.string.email_address)
            OutlinedTextField(
                shape = MaterialTheme.shapes.small,
                value = "",
                onValueChange = {},
                placeholder = { Text("Example: johndoe@gmail.com" , color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))


            TextFieldLabel(R.string.password)
            OutlinedTextField(
                shape = MaterialTheme.shapes.small,
                value = "",
                onValueChange = {},
                placeholder = { Text("******", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))


            TextFieldLabel(R.string.retype_password)
            OutlinedTextField(
                shape = MaterialTheme.shapes.small,
                value = "",
                onValueChange = {},
                placeholder = { Text("******" , color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = { /* Handle registration */ },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.register),
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // OR Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(
                    text = stringResource(R.string.or),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Google Register Button
            OutlinedButton(
                onClick = { /* Handle Google registration */ },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Icon(
                    painter = painterResource(R.drawable.google_logo),
                    contentDescription = stringResource(R.string.google_logo),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Register with Google",
                    modifier = Modifier.padding(8.dp) ,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.already_have_account_login),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* Handle register */ }
            )
        }
    }
}


@Composable
@Preview
fun SingUpScreenPreview(){
    NotesMakerTheme {
        SignupScreen()
    }
}
