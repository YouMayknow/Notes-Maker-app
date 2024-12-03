package com.example.notesMaker.ui.screen.entryScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesMaker.R

@Composable
fun CreateNewPasswordScreen(onBackToLoginClick: () -> Unit, onCreatePasswordClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.back_to_login),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .clickable { onBackToLoginClick() }
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.create_a_new_password),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.your_new_password_should_be_different_from_the_previous_password),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.new_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.min_8_characters_combination_of_0_9_a_z_a_z),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.retype_new_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = onCreatePasswordClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Create Password")
        }
    }
}

@Composable
@Preview
private  fun CreateNewPasswordScreenPreview(){
    CreateNewPasswordScreen({}) { }
}